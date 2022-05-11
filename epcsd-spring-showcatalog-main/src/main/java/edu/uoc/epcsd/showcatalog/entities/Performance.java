package edu.uoc.epcsd.showcatalog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * As the PR1 solution suggest in the definition of the operation
 *  "createPerformance(showId, date, isPublic, capacity, price)",
 * Performance class should have the following attributes: isPublic, capacity, price.
 *
 * However, I decided to exclude them due to its lack of use and to match the definition of
 * the Performance class given in the PR2 statement.
 */

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private Timestamp time;

    @URL
    @Column(name = "streamingURL")
    private String streamingURL;

    @Column(name = "remainingSeats")
    private int remainingSeats; // duration in minutes

    /**
     * En referencia al caso de estudio: las Actuaciones no tendrán estado y por lo tanto no es necesario
     * implementar ninguna operación para cancelar Actuaciones individuales.
     */
    @Column(name = "status")
    private String status;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "show_id")
    private Show show;

}
