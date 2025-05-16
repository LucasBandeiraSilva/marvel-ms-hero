package com.github.lucasbandeira.msmarvel.resources;

import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @GetMapping
    public ResponseEntity<List<HeroResponseDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(heroService.getAllHeroes());
    }

    @GetMapping("/by-agent/{agentCode}")
    public ResponseEntity<List<HeroResponseDTO>> getHeroesByAgent(@PathVariable String agentCode) {
        List<HeroResponseDTO> heroList = heroService.getHeroesByAgent(agentCode);

        if (heroList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(heroList);
    }


    @PostMapping
    public ResponseEntity<Void>saveHero( @RequestBody @Valid HeroRequestDTO heroRequestDto ){
        Hero hero = Hero.fromDTO(heroRequestDto);
        heroService.saveHero(hero);
        return ResponseEntity.created(generateURI(hero.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroResponseDTO>getById( @PathVariable UUID id ){
        Hero hero = heroService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(HeroResponseDTO.fromEntity(hero));
    }

    @PutMapping("/{heroCode}")
    public ResponseEntity<Void>UpdateHeroById(@PathVariable String heroCode, @RequestBody @Valid HeroRequestDTO heroRequestDto){
        heroService.updateHero(heroCode, heroRequestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteHero(@PathVariable UUID id){
        heroService.deleteHero(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping(params = "hero-code")
    public ResponseEntity<HeroResponseDTO> getHeroByCode(@RequestParam("hero-code")String heroCode){
        Optional <HeroResponseDTO> hero = heroService.getHeroByCode(heroCode);
        if (hero.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).body(hero.get());
    }

}
