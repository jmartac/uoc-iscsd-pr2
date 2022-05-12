package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dtos.PerformanceDTO;
import edu.uoc.epcsd.showcatalog.dtos.ShowDTO;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.service.CatalogService;
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
    private CatalogService catalogService;

    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    @GetMapping()
    public ResponseEntity<List<Show>> findShowsByNameOrCategory(@RequestParam(required = false) String name, @RequestParam(required = false) Optional<Long> categoryId) {
        log.trace("findShowsByNameOrCategory");

        return catalogService.findShowsByNameOrCategory(name, categoryId);
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

        // TODO Notify Kafka?

        return catalogService.createShow(show);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<Show> getShowDetails(@PathVariable long showId) {
        log.trace("getShowDetails");

        return catalogService.getShowDetails(showId);
    }

    @GetMapping("/{showId}/performances")
    public ResponseEntity<List<Performance>> findShowPerformances(@PathVariable long showId) {
        log.trace("findShowPerformances");

        return catalogService.findShowPerformances(showId);
    }

    @PostMapping("/{showId}/performances")
    public ResponseEntity<String> createPerformance(@PathVariable long showId, @RequestBody PerformanceDTO requestBody) {
        log.trace("createPerformance");

        if (requestBody.getStreamingURL() == null || requestBody.getStreamingURL().isEmpty()) {
            log.warn("Streaming URL is required");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Performance performance = new Performance();
        performance.setStreamingURL(requestBody.getStreamingURL());
        performance.setDate(requestBody.getDate());
        performance.setTime(requestBody.getTime());
        performance.setRemainingSeats(requestBody.getRemainingSeats());

        return catalogService.createPerformance(showId, performance);
    }

    @PostMapping("/{showId}/edit")
    public ResponseEntity<Show> addShowCategory(@PathVariable long showId, @RequestParam long categoryId) {
        log.trace("addShowCategory");

        return catalogService.addShowCategory(showId, categoryId);
    }
}
