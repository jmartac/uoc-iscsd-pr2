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

    @JsonIgnore
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

    @JsonIgnore
    @Column(name = "status")
    private String status;

    /**
     * Como ya he dicho, se aceptará indistintamente cualquiera de las dos implementaciones (el modelo de la solución
     * corresponde a tu interpretación, donde la relación es 1 a N).
     */
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "show_categories",
            joinColumns = @JoinColumn(name = "id_show"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    @ToString.Exclude
    private List<Category> categories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "show")
    @OrderBy("id.id_show DESC, id.streamingURL")
    @ToString.Exclude
    private List<Performance> performances = new java.util.ArrayList<>();

    /**
     * En referencia a la implementación del comando POST /shows/{showId}/performances/{performanceId}/cancel:
     * no se debe implementar esta operación
     */
}
