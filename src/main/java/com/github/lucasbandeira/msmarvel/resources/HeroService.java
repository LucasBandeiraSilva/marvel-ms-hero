package com.github.lucasbandeira.msmarvel.resources;

import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDto;
import com.github.lucasbandeira.msmarvel.repository.HeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    public Hero saveHero(Hero hero){
        return heroRepository.save(hero);
    }

    public Hero getById( UUID id ){
        return heroRepository.findById(id).orElseThrow(()-> new RuntimeException("Hero not found"));
    }

    public Optional<Hero>updateHero( UUID id, HeroRequestDto heroRequestDto ){
        return heroRepository.findById(id).map(existingHero  ->{
            Hero hero = Hero.fromDTO(heroRequestDto);
            hero.setId(existingHero.getId());
            return heroRepository.save(hero);
        });
    }

    public void deleteHero(UUID id){
        heroRepository.deleteById(id);
    }
}
