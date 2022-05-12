package edu.uoc.epcsd.showcatalog.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryDTO {

    @NotBlank
    private String name;

    private String description;

}
