package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MatchDto {
    private UUID id;

    @NotBlank(message = "home club cannot be empty")
    private String homeClub;

    @NotBlank(message = "visiting club cannot be empty")
    private String visitingClub;

    @NotBlank(message = "stadium club cannot be empty")
    private String stadium;

    @NotBlank(message = "date and hour club cannot be empty")
    private String dateTime;

    @NotNull(message = "home goals cannot be empty")
    @PositiveOrZero(message = "home goals must be greater than or equal to zero")
    private Integer homeGoals;

    @NotNull(message = "visiting goals cannot be empty")
    @PositiveOrZero(message = "visiting goals must be greater than or equal to zero")
    private Integer visitingGoals;

    public MatchDto(String homeClub, String visitingClub, String stadium, String dateTime, Integer homeGoals, Integer visitingGoals) {
        this.homeClub = homeClub;
        this.visitingClub = visitingClub;
        this.stadium = stadium;
        this.dateTime = dateTime;
        this.homeGoals = homeGoals;
        this.visitingGoals = visitingGoals;
    }
}
