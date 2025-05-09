package com.github.lucasbandeira.msmarvel.model;

import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public Hero( String heroCode, String name, List <String> skills, Integer age, List <String> characteristics ) {
        this.heroCode = heroCode;
        this.name = name;
        this.skills = skills;
        this.age = age;
        this.characteristics = characteristics;
    }

    public static Hero fromDTO( HeroRequestDTO heroRequestDto ){
        Hero hero = new Hero();
        hero.heroCode = heroRequestDto.heroCode();
        hero.name = heroRequestDto.name();
        hero.skills = heroRequestDto.skills();
        hero.age = heroRequestDto.age();
        hero.characteristics = heroRequestDto.characteristics();
        return hero;
    }


}
