package br.com.meli.matchsaver.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MatchDto(
        @NotBlank(message = "home club cannot be empty")
        String homeClub,
        @NotBlank(message = "visiting club cannot be empty")
        String visitingClub,
        @NotBlank(message = "stadium club cannot be empty")
        String stadium,
        @NotBlank(message = "date and hour club cannot be empty")
        String dateTime,
        @NotNull(message = "home goals cannot be empty")
        @PositiveOrZero(message = "home goals must be greater than or equal to zero")
        Integer homeGoals,
        @NotNull(message = "visiting goals cannot be empty")
        @PositiveOrZero(message = "visiting goals must be greater than or equal to zero")
        Integer visitingGoals){
}
