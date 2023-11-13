package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StadiumDto {
    private UUID id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotNull(message = "capacity cannot be null")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Long capacity;

    public StadiumDto(String name, Long capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
