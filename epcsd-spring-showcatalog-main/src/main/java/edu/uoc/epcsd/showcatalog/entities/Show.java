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

    @Column(name = "duration")
    private int duration; // duration in minutes

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "onSaleDate")
    private LocalDate onSaleDate;

    @Column(name = "status")
    private LocalDate status;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "show_categories",
            joinColumns = @JoinColumn(name = "id_show"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    @ToString.Exclude
    private List<Category> categories;

    //@JsonIgnore
    @OneToMany(mappedBy = "show")
    @ToString.Exclude
    private List<Performance> performances;

    /**
     * En referencia a la implementación del comando POST /shows/{showId}/performances/{performanceId}/cancel:
     * no se debe implementar esta operación
     */
}
