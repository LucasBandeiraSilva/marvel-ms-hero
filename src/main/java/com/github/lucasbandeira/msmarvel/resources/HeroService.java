package com.github.lucasbandeira.msmarvel.resources;

import com.github.lucasbandeira.msmarvel.exception.HeroNotFoundException;
import com.github.lucasbandeira.msmarvel.infra.repository.HeroRepository;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;
import com.github.lucasbandeira.msmarvel.validator.HeroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final HeroValidator validator;

    public Hero saveHero( Hero hero ) {
        validator.validateHero(hero);
        return heroRepository.save(hero);
    }

    public Hero getById( UUID id ) {
        return heroRepository.findById(id).orElseThrow(() -> new HeroNotFoundException("The hero you requested was not found"));
    }

    public Optional<Hero> updateHero(String heroCode, HeroRequestDTO dto) {
        Optional<Hero> heroOptional = heroRepository.findByHeroCode(heroCode);
        if (!heroOptional.isPresent()){
            throw new HeroNotFoundException("The hero you requested was not found");
        }

        return heroOptional.map(existingHero -> {
            validator.validateHero(existingHero);
            existingHero.setName(dto.name());
            existingHero.setSkills(dto.skills());
            existingHero.setAge(dto.age());
            existingHero.setCharacteristics(dto.characteristics());
            return heroRepository.save(existingHero);
        });
    }


    public void deleteHero( UUID id ) {
        Hero hero = heroRepository.findById(id).orElseThrow(() -> new HeroNotFoundException("The hero you requested was not found"));
        heroRepository.delete(hero);
    }

    public Optional <HeroResponseDTO> getHeroByCode( String heroCode ) {
        return Optional.ofNullable(heroRepository.findByHeroCode(heroCode).map(hero -> {
            HeroResponseDTO heroResponseDTO = new HeroResponseDTO(
                    hero.getHeroCode(),
                    hero.getName(),
                    hero.getSkills(),
                    hero.getAge(),
                    hero.getCharacteristics()

            );
            return heroResponseDTO;
        }).orElseThrow(() -> new HeroNotFoundException("The hero you requested was not found")));
    }

    public List<HeroResponseDTO> getHeroesByAgent( String agentCode) {
        List <Hero> heroes = heroRepository.findByAgent_AgentCode(agentCode);

        return heroes.stream()
                .map(hero -> new HeroResponseDTO(
                        hero.getHeroCode(),
                        hero.getName(),
                        hero.getSkills(),
                        hero.getAge(),
                        hero.getCharacteristics()
                ))
                .collect(Collectors.toList());
    }



    public List <HeroResponseDTO> getAllHeroes() {
        return heroRepository.findAll().stream()
                .map(hero ->
                        new HeroResponseDTO(
                                hero.getHeroCode(),
                                hero.getName(),
                                hero.getSkills(),
                                hero.getAge(),
                                hero.getCharacteristics()
                        ))
                .collect(Collectors.toList());

    }

}
