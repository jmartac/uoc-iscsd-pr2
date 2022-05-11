package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Show> showDetails(@PathVariable long id) {
        log.trace("showDetails");

        return showRepository.findById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public long createShow(@RequestBody Show body) {
        log.trace("createShow");

        Show show = new Show();
        show.setName(body.getName());
        show.setDescription(body.getDescription());
        show.setImage(body.getImage());
        show.setPrice(body.getPrice());
        show.setCapacity(body.getCapacity());
        show.setDuration(body.getDuration());
        show.setStatus("CREATED");

        // TODO Notificar

        return showRepository.save(show).getId();
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

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String createPerformance(@PathVariable long id, @RequestBody Performance body) {
        log.trace("createPerformance");

        Show show = showRepository.findById(id).orElse(null);
        if (show == null) {
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).build());
        }

        Performance performance = new Performance();
        performance.setShow(show);
        performance.setDate(body.getDate());
        performance.setStatus("CREATED");

        show.getPerformances().add(performance);

        return showRepository.save(show).getPerformances().get(0).getId().toString();
    }

}
