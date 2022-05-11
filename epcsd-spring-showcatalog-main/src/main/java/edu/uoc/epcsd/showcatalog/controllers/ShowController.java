package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.DTOs.PerformanceDTO;
import edu.uoc.epcsd.showcatalog.DTOs.ShowDTO;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.PerformancePK;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * TODO fix Bad Request exceptions
     * TODO use DTO instead of Show classes
     */

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    @PostMapping("/")
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
    public ResponseEntity<Show> showDetails(@PathVariable long showId) {
        log.trace("showDetails");

        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(show, HttpStatus.OK);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getShowsByName(@RequestParam String name) {
        log.trace("getShowByName");

        return showRepository.findShowByName(name);
    }

    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getShowsByCategory(@RequestParam long id) {
        log.trace("getShowsByCategory");

        return showRepository.findShowByCategoriesId(id);
    }

    @PostMapping("/{showId}")
    public ResponseEntity<PerformancePK> createPerformance(@PathVariable long showId, @RequestBody PerformanceDTO requestBody) {
        log.trace("createPerformance");

        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Performance performance = new Performance();
        performance.setShow(show);
        performance.setDate(requestBody.getDate());
        performance.setRemainingSeats(requestBody.getRemainingSeats());
        performance.setStatus("CREATED");

        show.addPerformance(performance);

        Performance saved = showRepository.save(show).mostRecentPerformance();
        log.info("Performance {} created for Show {}", saved.getId(), show.getId());

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

}
