package br.com.meli.matchsaver.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record MatchDto(
        @NotNull
        String homeClub,
        @NotNull
        String visitingClub,
        @NotNull
        String stadium,
        @NotNull
        String dateTime,
        @NotNull @PositiveOrZero
        Integer homeGoals,
        @NotNull @PositiveOrZero
        Integer visitingGoals){
}
