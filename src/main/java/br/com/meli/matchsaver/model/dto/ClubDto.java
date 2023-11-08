package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ClubDto(
        @NotBlank(message = "name cannot be empty")
        String name) {
}
