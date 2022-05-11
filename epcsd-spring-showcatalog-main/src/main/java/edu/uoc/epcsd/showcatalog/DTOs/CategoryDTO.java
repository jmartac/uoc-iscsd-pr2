package edu.uoc.epcsd.showcatalog.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryDTO {

    private Long id;

    @NotBlank
    private String name;

    private String description;

}
