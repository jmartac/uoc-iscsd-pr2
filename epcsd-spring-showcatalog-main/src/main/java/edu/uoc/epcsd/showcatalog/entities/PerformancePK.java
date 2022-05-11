package edu.uoc.epcsd.showcatalog.entities;


import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PerformancePK implements Serializable {

    private Show show;

    private LocalDate date;

    private String streamingURL;
}
