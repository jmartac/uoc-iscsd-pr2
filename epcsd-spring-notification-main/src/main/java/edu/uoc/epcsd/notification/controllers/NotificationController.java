package edu.uoc.epcsd.notification.controllers;

import edu.uoc.epcsd.notification.pojos.Show;
import edu.uoc.epcsd.notification.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final String CatalogServiceUrl = "http://localhost:18081/";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/notify")
    public ResponseEntity<Object> notify(@RequestParam Long showId) {
        log.trace("notify");

        Show show;
        try {
            // Get show from CatalogService
            show = restTemplate.getForObject(CatalogServiceUrl + "shows/" + showId, Show.class);
        } catch (HttpStatusCodeException e) {
            String logMsg = "Show could not be retrieved, CatalogService response: " + e.getStatusCode();
            logMsg = e.getStatusCode() == HttpStatus.NOT_FOUND ? "Requested Show was not found" : logMsg;
            log.warn(logMsg);
            return new ResponseEntity<>(e.getStatusCode());
        }
        return notificationService.notify(show);
    }

}
