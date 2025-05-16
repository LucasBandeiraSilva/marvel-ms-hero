package com.github.lucasbandeira.msmarvel.common;

import com.github.lucasbandeira.msmarvel.model.Agent;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;

import java.util.List;
import java.util.UUID;

public class HeroConstants {

    public static Hero createHero(){
        return new Hero(
                "Marvel-001",
                "Punisher",
                List.of("Expert marksmanship", "Hand-to-hand combat", "Tactical strategy"),
                38,
                List.of("Vengeful", "Relentless", "Strategic")
        );
    }

    public static Hero createInvalidHero(){
        return new Hero(
                "Marvel-001",
                "Punisher",
                List.of("Expert marksmanship", "Hand-to-hand combat"),
                38,
                List.of("Vengeful", "Relentless")
        );
    }



    public static HeroResponseDTO createHeroResponseDTO(){
        return new HeroResponseDTO(
                "Marvel-001",
                "Punisher",
                List.of("Expert marksmanship", "Hand-to-hand combat", "Tactical strategy"),
                38,
                List.of("Vengeful", "Relentless", "Strategic")
        );
    }

    public static HeroRequestDTO createHeroRequestDTO(){
        return new HeroRequestDTO(
                "Marvel-003",
                "Bucky Barnes",
                List.of("Enhanced strength", "Expert marksman", "Combat skills", "Cybernetic arm"),
                100,
                List.of("Loyal", "Haunted", "Resilient")
        );
    }

    public static HeroRequestDTO createInvalidHeroRequestDTO(){
        return new HeroRequestDTO(
                "",
                " ",
                List.of("fly"),
                null,
                List.of()
        );
    }

    public static Agent createAgent(){
        return new Agent(
                null,
                "AGENT-001",
                "Nick Fury",
                true
        );
    }


}
