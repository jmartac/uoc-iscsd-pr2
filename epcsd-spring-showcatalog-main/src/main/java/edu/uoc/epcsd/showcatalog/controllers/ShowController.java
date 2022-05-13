package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dtos.PerformanceDTO;
import edu.uoc.epcsd.showcatalog.dtos.ShowDTO;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.showcatalog.services.CatalogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
 *
 * I understand that Performances do not need to be cancelled/deleted:
 * Foro: "En referencia a la implementación del comando POST /shows/{showId}/performances/{performanceId}/cancel
 * no se debe implementar esta operación"
 *
 * I understand that shows can be deleted instead of cancelled:
 * Foro: "en lo que respecta a los Actos, entiendo que pueda dar lugar a las dos interpretaciones:
 * cambio de estado / borrado de la BBDD. En este caso se aceptaría cualquiera de las dos,
 * mientras sea consistente con el resto del ejercicio"
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
    public ResponseEntity<Long> createShow(@RequestParam(required = false) Optional<List<Long>> categoriesIds, @RequestBody ShowDTO requestBody) {
        log.trace("createShow");

        Show show = new Show();
        show.setName(requestBody.getName());
        show.setDescription(requestBody.getDescription());
        show.setImage(requestBody.getImage());
        show.setPrice(requestBody.getPrice());
        show.setCapacity(requestBody.getCapacity());
        show.setDuration(requestBody.getDuration());
        show.setStatus("CREATED");

        Show saved = catalogService.createShow(categoriesIds.orElse(new ArrayList<>()), show);

        // Notify users who have any of the given categories as favourite that a new show has been created
        log.info("Sending message to Kafka topic {}", KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD);
        kafkaTemplate.send(KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD, saved);

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<Show> getShowDetails(@PathVariable long showId) {
        log.trace("getShowDetails");

        return catalogService.getShowDetails(showId);
    }

    @PostMapping("/{showId}/open")
    public ResponseEntity<Show> openShow(@PathVariable long showId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> onSaleDate) {
        log.trace("openShow");

        return catalogService.openShow(showId, onSaleDate.orElse(LocalDate.now()));
    }

    // I decided to delete shows, instead of cancel them (it was said in the forum that we could choose).
    @PostMapping("/{showId}/delete")
    public ResponseEntity<Object> deleteShow(@PathVariable long showId) {
        log.trace("deleteShow");

        return catalogService.deleteShow(showId);
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

    @PostMapping("/{showId}/addCategory")
    public ResponseEntity<Show> addShowCategory(@PathVariable long showId, @RequestParam long categoryId) {
        log.trace("addShowCategory");

        return catalogService.addShowCategory(showId, categoryId);
    }
}
