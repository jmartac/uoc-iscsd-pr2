package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dtos.PerformanceDTO;
import edu.uoc.epcsd.showcatalog.dtos.ShowDTO;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * As the PR1 solution suggest in the definition of the operation
 *  "createShow(categoryId, name, description, image, price, capacity, duration)",
 * Show class should have one and only one categoryId.
 *
 * However, as the Show class given in the PR2 statement (and code base) does have multiple categories,
 * I decided to implement the given ManyToMany relationship between Category and Show.
 * Therefore, the createShow operation will not ask for a categoryId.
 */

@Log4j2
@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    /**
     * This operation only accepts one of two request parameters:
     * @param name
     * @param categoryId
     * @return Bad Request if both parameters, or neither, are provided
     */
    @GetMapping()
    public ResponseEntity<List<Show>> findShowsByNameOrCategory(@RequestParam(required = false) String name, @RequestParam(required = false) Optional<Long> categoryId) {
        log.trace("findShowsByNameOrCategory");

        if ((name != null && !name.isBlank() && categoryId.isPresent())
                        || ((name == null || name.isBlank()) && categoryId.isEmpty())
        ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (categoryId.isPresent()) {
            return new ResponseEntity<>(showRepository.findShowByCategoriesId(categoryId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(showRepository.findByNameContaining(name), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Long> createShow(@RequestBody ShowDTO requestBody) {
        log.trace("createShow");

        Show show = new Show();
        show.setName(requestBody.getName());
        show.setDescription(requestBody.getDescription());
        show.setImage(requestBody.getImage());
        show.setPrice(requestBody.getPrice());
        show.setCapacity(requestBody.getCapacity());
        show.setDuration(requestBody.getDuration());
        show.setStatus("CREATED");

        Show saved = showRepository.save(show);
        log.info("Show {} created", saved.getId());

        // TODO Notify Kafka

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<Show> getShowDetails(@PathVariable long showId) {
        log.trace("getShowDetails");

        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(show, HttpStatus.OK);
    }

    @PostMapping("/{showId}/performances")
    public ResponseEntity<String> createPerformance(@PathVariable long showId, @RequestBody PerformanceDTO requestBody) {
        log.trace("createPerformance");

        if (requestBody.getStreamingURL() == null || requestBody.getStreamingURL().isEmpty()) {
            log.warn("Streaming URL is required");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Performance performance = new Performance();
        performance.setShow(show);
        performance.setStreamingURL(requestBody.getStreamingURL());
        performance.setDate(requestBody.getDate());
        performance.setTime(requestBody.getTime());
        performance.setRemainingSeats(requestBody.getRemainingSeats());
        performance.setStatus("CREATED");

        show.addPerformance(performance);

        Show showSaved = showRepository.save(show);
        Performance performanceSaved = showSaved.findPerformance(performance);

        log.info("Performance {} created for Show {}", performanceSaved.idToString(), showSaved.getId());
        return new ResponseEntity<>(performanceSaved.idToString(), HttpStatus.OK);
    }

}
