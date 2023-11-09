package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "capacity cannot be empty")
    private Long capacity;

    public StadiumDto(String name, Long capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
