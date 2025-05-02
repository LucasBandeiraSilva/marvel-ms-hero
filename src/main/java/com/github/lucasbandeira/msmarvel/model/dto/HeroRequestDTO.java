package com.github.lucasbandeira.msmarvel.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record HeroRequestDTO(
        @NotBlank(message = "You must provide the hero code!")
        @Column(unique = true)
        String heroCode,
        @NotBlank(message = "The name must not be null or blank!") String name,
        @NotNull(message = "The Skill list must not be null!")
        @Size(min = 2,message = "You must provide at least 2 skills!")
        List <String> skills,
        @NotNull(message = "You must provide the hero's age!")
        Integer age,
        @NotNull(message = "The characteristics list must not be null!")
        @Size(min = 2,message = "You must provide at least 2 characteristics of the hero!")
        List<String> characteristics) {
}
