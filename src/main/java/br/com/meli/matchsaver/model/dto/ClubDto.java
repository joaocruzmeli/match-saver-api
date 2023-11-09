package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClubDto {

    private UUID id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    public ClubDto(String name) {
        this.name = name;
    }
}
