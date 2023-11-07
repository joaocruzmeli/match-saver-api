package br.com.meli.matchsaver.model.dto;

import jakarta.validation.constraints.NotNull;

public record ClubDto(
        @NotNull
        String name) {
}
