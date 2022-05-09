package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getAllShows() {
        log.trace("getAllShows");

        return showRepository.findAll();
    }

    // add the code for the missing system operations here
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Show> showDetails(@PathVariable long id) {
        log.trace("getShow");

        return showRepository.findById(id);
    }

}
