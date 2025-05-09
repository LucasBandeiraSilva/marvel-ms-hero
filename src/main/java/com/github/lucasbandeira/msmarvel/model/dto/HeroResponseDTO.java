package com.github.lucasbandeira.msmarvel.model.dto;

import com.github.lucasbandeira.msmarvel.model.Hero;

import java.util.List;

public record HeroResponseDTO(String heroCode, String name, List <String> skills, Integer age, List<String> characteristics) {

    public static HeroResponseDTO fromEntity( Hero hero ){
        return new HeroResponseDTO(hero.getHeroCode(), hero.getName(), hero.getSkills(), hero.getAge(),hero.getCharacteristics());
    }


    }

