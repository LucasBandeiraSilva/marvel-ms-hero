package com.github.lucasbandeira.msmarvel.infra.repository;

import com.github.lucasbandeira.msmarvel.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HeroRepository extends JpaRepository<Hero, UUID> {

    Optional<Hero> findByHeroCode( String heroCode);
}
