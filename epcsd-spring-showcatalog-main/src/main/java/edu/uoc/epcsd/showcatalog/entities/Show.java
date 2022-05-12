package edu.uoc.epcsd.showcatalog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private double price;

    @Column(name = "duration")
    private double duration; // duration in minutes

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "onSaleDate")
    private LocalDate onSaleDate; // TODO: this field is always null both in the database and in the frontend

    @Column(name = "status")
    private String status;

    /**
     * Como ya he dicho, se aceptará indistintamente cualquiera de las dos implementaciones (el modelo de la solución
     * corresponde a tu interpretación, donde la relación es 1 a N).
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "show_categories",
            joinColumns = @JoinColumn(name = "id_show"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    @ToString.Exclude
    private List<Category> categories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "show", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    private List<Performance> performances = new ArrayList<>();

    public void addPerformance(Performance performance) {
        performances.add(performance);
    }

    public Performance findPerformance(Performance performance) {
        int i = performances.indexOf(performance);
        return i == -1 ? null : performances.get(i);
    }

}
