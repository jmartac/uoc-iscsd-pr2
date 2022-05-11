package edu.uoc.epcsd.showcatalog.DTOs;

import edu.uoc.epcsd.showcatalog.entities.PerformancePK;
import edu.uoc.epcsd.showcatalog.entities.Show;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PerformanceDTO {

    private PerformancePK id;

    private Show show;

    private LocalDate date;

    private int remainingSeats;

}
