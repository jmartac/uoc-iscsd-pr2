package edu.uoc.epcsd.showcatalog.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ShowDTO {

    private String name;

    private String description;

    private String image;

    private double price;

    private double duration;

    private int capacity;

    private LocalDate onSaleDate;

}
