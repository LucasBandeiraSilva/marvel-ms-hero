package com.github.lucasbandeira.msmarvel.resources;

import com.github.lucasbandeira.msmarvel.exception.HeroNotFoundException;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.infra.repository.HeroRepository;
import com.github.lucasbandeira.msmarvel.validator.HeroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final HeroValidator validator;

    public Hero saveHero(Hero hero){
        validator.validateHero(hero);
        return heroRepository.save(hero);
    }

    public Hero getById( UUID id ){
        return heroRepository.findById(id).orElseThrow(()-> new HeroNotFoundException("The hero you requested was not found"));
    }

    public Optional<Hero>updateHero( UUID id, HeroRequestDTO heroRequestDto ){
        return heroRepository.findById(id).map(existingHero  ->{
            Hero hero = Hero.fromDTO(heroRequestDto);
            validator.validateHero(hero);
            hero.setId(existingHero.getId());
            return heroRepository.save(hero);
        });
    }

    public void deleteHero(UUID id){
        Hero hero = heroRepository.findById(id).orElseThrow(() -> new HeroNotFoundException("The hero you requested was not found"));
        heroRepository.delete(hero);
    }

    public Optional<Hero>getHeroByCode(String heroCode){
        return heroRepository.findByHeroCode(heroCode);
    }
}
