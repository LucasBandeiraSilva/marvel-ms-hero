package com.github.lucasbandeira.msmarvel.model.dto;

import com.github.lucasbandeira.msmarvel.model.Hero;

import java.util.List;

public record HeroResponseDto(String heroCode, String name, List <String> skills,Integer age,List<String> characteristics) {

    public static HeroResponseDto fromEntity( Hero hero ){
        return new HeroResponseDto(hero.getHeroCode(), hero.getName(), hero.getSkills(), hero.getAge(),hero.getCharacteristics());
    }
}
