package br.com.meli.matchsaver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Clube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "clubeMandante")
    private List<Partida> partidasMandante;

    @OneToMany(mappedBy = "clubeVisitante")
    private List<Partida> partidasVisitante;
}
