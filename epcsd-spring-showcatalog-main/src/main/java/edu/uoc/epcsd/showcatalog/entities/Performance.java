package edu.uoc.epcsd.showcatalog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

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
    @Column(name = "state")
    private String state;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "show_id")
    private Show show;

}
