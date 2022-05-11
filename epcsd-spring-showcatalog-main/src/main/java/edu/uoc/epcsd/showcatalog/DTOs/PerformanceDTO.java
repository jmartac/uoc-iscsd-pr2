package edu.uoc.epcsd.showcatalog.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class PerformanceDTO {

    private String streamingURL;

    private LocalDate date;

    private Timestamp time;

    private int remainingSeats;
}
