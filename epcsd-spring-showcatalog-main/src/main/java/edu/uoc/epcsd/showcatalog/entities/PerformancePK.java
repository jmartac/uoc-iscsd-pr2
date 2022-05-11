package edu.uoc.epcsd.showcatalog.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PerformancePK implements Serializable {

    private String id_show;

    @URL
    @Column(name = "streaming_URL")
    private String streamingURL;
}
