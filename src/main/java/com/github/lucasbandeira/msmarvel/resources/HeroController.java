package com.github.lucasbandeira.msmarvel.resources;

import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDto;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hero")
public class HeroController implements BuildLocationUri {

    private final HeroService heroService;

    @GetMapping("/status")
    public String status(){
        return "Ok";
    }
    @PostMapping
    public ResponseEntity<Void>saveHero( @RequestBody HeroRequestDto heroRequestDto ){
        Hero hero = Hero.fromDTO(heroRequestDto);
        heroService.saveHero(hero);
        return ResponseEntity.created(generateURI(hero.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroResponseDto>getById( @PathVariable UUID id ){
        Hero hero = heroService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(HeroResponseDto.fromEntity(hero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void>UpdateHeroById(@PathVariable UUID id, @RequestBody HeroRequestDto heroRequestDto){
        heroService.updateHero(id, heroRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteHero(@PathVariable UUID id){
        heroService.deleteHero(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
