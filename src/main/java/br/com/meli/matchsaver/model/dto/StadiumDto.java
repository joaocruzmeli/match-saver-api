package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotBlank;

public record StadiumDto(
        @NotBlank(message = "name cannot be empty")
        String name,
        Long capacity) {
}
