package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotNull;

public record StadiumDto(
        @NotNull
        String name,
        Long capacity) {
}
