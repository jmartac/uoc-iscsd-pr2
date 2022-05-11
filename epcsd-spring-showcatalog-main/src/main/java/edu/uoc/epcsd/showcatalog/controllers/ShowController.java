package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.PerformanceRepository;
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

@Log4j2
@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private PerformanceRepository performanceRepository;

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
    public long createShow(@RequestParam long categoryId, @RequestBody Show body) {
        log.trace("createShow");

//        Category category = categoryRepository.findById(categoryId).orElse(null);
//        if (category == null) {
//            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).build());
//        }

        Show show = new Show();
//        show.setCategory(category);
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

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getShowsByName(@RequestParam String category) {
        log.trace("getShowByName");

        return showRepository.findByName(category);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getShowsByCategory(long categoryId) {
        log.trace("getShowsByCategory");

        return showRepository.findByCategoryId(categoryId);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public long createPerformance(@PathVariable long id, @RequestBody Performance body) {
        log.trace("createPerformance");

        Show show = showRepository.findById(id).orElse(null);
        if (show == null) {
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).build());
        }

        Performance performance = new Performance();
        performance.setShow(show);
        performance.setDate(body.getDate());
        performance.setPublic(body.isPublic());
        performance.setCapacity(body.getCapacity());
        performance.setPrice(body.getPrice());
        performance.setStatus("CREATED");

        return performanceRepository.save(performance).getId();
    }

}
