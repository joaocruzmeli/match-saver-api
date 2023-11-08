package br.com.meli.matchsaver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_CLUBS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClubModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "homeClub")
    private List<MatchModel> homeMatchs;

    @JsonIgnore
    @OneToMany(mappedBy = "visitingClub")
    private List<MatchModel> visitingMatchs;
}
