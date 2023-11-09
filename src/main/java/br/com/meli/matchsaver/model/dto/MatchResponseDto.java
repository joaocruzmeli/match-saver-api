package br.com.meli.matchsaver.model.dto;

import br.com.meli.matchsaver.enums.Result;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDto {
    private UUID id;

    private ClubDto homeClub;

    private ClubDto visitingClub;

    private StadiumDto stadium;

    private String dateTime;

    private Integer homeGoals;

    private Integer visitingGoals;

    @Enumerated(EnumType.STRING)
    private Result result;
}
