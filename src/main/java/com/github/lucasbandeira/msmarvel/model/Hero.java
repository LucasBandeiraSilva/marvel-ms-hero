package com.github.lucasbandeira.msmarvel.model;

import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_hero")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String heroCode;
    private String name;
    private List<String> skills;
    private Integer age;
    private List<String> characteristics;

    public Hero( String heroCode, String name, List <String> skills, Integer age, List <String> characteristics ) {
        this.heroCode = heroCode;
        this.name = name;
        this.skills = skills;
        this.age = age;
        this.characteristics = characteristics;
    }

    public static Hero fromDTO( HeroRequestDto heroRequestDto ){
        Hero hero = new Hero();
        hero.heroCode = heroRequestDto.heroCode();
        hero.name = heroRequestDto.name();
        hero.skills = heroRequestDto.skills();
        hero.age = heroRequestDto.age();
        hero.characteristics = heroRequestDto.characteristics();
        return hero;
    }


}
