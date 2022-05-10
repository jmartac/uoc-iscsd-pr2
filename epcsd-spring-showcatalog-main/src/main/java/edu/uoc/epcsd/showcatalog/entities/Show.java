package edu.uoc.epcsd.showcatalog.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate onSaleDate;

    @Column(name = "status")
    private LocalDate status;

    /**
     * Como ya he dicho, se aceptará indistintamente cualquiera de las dos implementaciones (el modelo de la solución
     * corresponde a tu interpretación, donde la relación es 1 a N).
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    private Category category;

    //@JsonIgnore
    @OneToMany(mappedBy = "show")
    @ToString.Exclude
    private List<Performance> performances;

    /**
     * En referencia a la implementación del comando POST /shows/{showId}/performances/{performanceId}/cancel:
     * no se debe implementar esta operación
     */
}
