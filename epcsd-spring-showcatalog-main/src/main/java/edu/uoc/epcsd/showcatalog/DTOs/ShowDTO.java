package edu.uoc.epcsd.showcatalog.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ShowDTO {

    private Long id;

    private String name;

    private String description;

    private String image;

    private double price;

    private double duration;

    private int capacity;

    private LocalDate onSaleDate;

    private String status;

}
