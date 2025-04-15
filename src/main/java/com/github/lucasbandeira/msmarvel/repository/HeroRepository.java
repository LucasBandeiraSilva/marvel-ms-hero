package com.github.lucasbandeira.msmarvel.repository;

import com.github.lucasbandeira.msmarvel.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HeroRepository extends JpaRepository<Hero, UUID> {
}
