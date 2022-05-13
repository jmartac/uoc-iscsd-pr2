package edu.uoc.epcsd.showcatalog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
 *
 * Foro:
 * "En referencia al caso de estudio: las Actuaciones no tendrán estado y por lo tanto no es necesario
 * implementar ninguna operación para cancelar Actuaciones individuales."
 *
 */

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PerformancePK.class)
public class Performance {

    @JsonIgnore
    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_show")
    private Show show;

    @Id
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Id
    @Column(name = "streaming_URL")
    private String streamingURL;

    @EqualsAndHashCode.Exclude
    @Column(name = "time")
    private Timestamp time;

    @EqualsAndHashCode.Exclude
    @Column(name = "remainingSeats")
    private int remainingSeats;

    public String idToString() {
        return show.getId() + "-" + date.toString() + "-" + streamingURL;
    }
}
