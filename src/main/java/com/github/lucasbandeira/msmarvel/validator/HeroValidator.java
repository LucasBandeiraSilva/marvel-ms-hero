package com.github.lucasbandeira.msmarvel.validator;

import com.github.lucasbandeira.msmarvel.exception.DuplicateHeroException;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.repository.HeroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HeroValidator {

    private final HeroRepository heroRepository;


    public void validateHero(Hero hero){
        if (isHeroRegistered(hero)) throw new DuplicateHeroException("This hero is already registered!");
    }

    private boolean isHeroRegistered( Hero hero){
        Optional <Hero> heroOptional = heroRepository.findByHeroCode(hero.getHeroCode());
        if (hero.getId()==null){
            return heroOptional.isPresent();
        }
        return heroOptional.isPresent() && !hero.getId().equals(heroOptional.get().getId());
    }
}
