package br.com.meli.matchsaver.model;

import br.com.meli.matchsaver.enums.Result;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private ClubModel homeClub;

    @ManyToOne
    private ClubModel visitingClub;

    private Integer homeGoals;

    private Integer visitingGoals;

    @Enumerated(EnumType.STRING)
    private Result result;

    @ManyToOne
    private StadiumModel stadiumModel;

}
