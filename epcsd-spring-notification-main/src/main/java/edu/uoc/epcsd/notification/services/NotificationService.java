package edu.uoc.epcsd.notification.services;

import edu.uoc.epcsd.notification.pojos.Category;
import edu.uoc.epcsd.notification.pojos.Show;
import edu.uoc.epcsd.notification.pojos.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NotificationService {

    @Autowired
    private UserService userService;    // mock service

    public void notifyShowCreation(Show show) {
        // Notify users that a new show has been created
        for (Category category : show.getCategories()) {
            for (User user : userService.getUsersByFavouriteCategory(category)) {
                notifyUser(user, show);
            }
        }
    }

    public ResponseEntity<Object> notify(Show show) {
        // Manually notify users that a new show has been created
        log.info("Manually notifying users that a new show has been added");
        notifyShowCreation(show);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // mock notification
    private void notifyUser(User user, Show show) {
        // send email / push notification / etc.
        log.info("Show \"" + show.getName() + "\" added!. Notifying the user \"" + user.getFullName() + "\"");
    }
}
