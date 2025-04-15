package com.github.lucasbandeira.msmarvel.model.dto;

import java.util.List;

public record HeroRequestDto(String heroCode, String name, List <String> skills, Integer age, List<String> characteristics) {
}
