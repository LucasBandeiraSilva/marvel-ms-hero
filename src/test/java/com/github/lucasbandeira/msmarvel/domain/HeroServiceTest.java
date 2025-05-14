package com.github.lucasbandeira.msmarvel.domain;

import com.github.lucasbandeira.msmarvel.common.HeroConstants;
import com.github.lucasbandeira.msmarvel.exception.DuplicateHeroException;
import com.github.lucasbandeira.msmarvel.exception.HeroNotFoundException;
import com.github.lucasbandeira.msmarvel.infra.repository.HeroRepository;
import com.github.lucasbandeira.msmarvel.model.Agent;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;
import com.github.lucasbandeira.msmarvel.resources.HeroService;
import com.github.lucasbandeira.msmarvel.validator.HeroValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static com.github.lucasbandeira.msmarvel.common.HeroConstants.*;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {

    private Agent agent;
    private Hero hero;
    private HeroRequestDTO heroRequestDTO;
    private HeroResponseDTO heroResponseDTO;

    @InjectMocks
    private HeroService heroService;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private HeroValidator validator;

    private final UUID heroId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");


    @BeforeEach
    void setUp(){
        agent = createAgent();
        hero = createHero();
        heroRequestDTO = createHeroRequestDTO();
        heroResponseDTO = createHeroResponseDTO();
    }


    @Test
    public void createHero_withValidData_returnsHero(){
        when(heroRepository.save(hero)).thenReturn(hero);

        Hero sut = heroService.saveHero(hero);

        assertThat(sut).isEqualTo(hero);
    }

    @Test
    public void createHero_WithInvalidData_ThrowsDuplicateHeroException(){
        doThrow(new DuplicateHeroException("This hero is already registered!"))
                .when(validator).validateHero(hero);

        assertThatThrownBy(()-> heroService.saveHero(hero))
                .isInstanceOf(DuplicateHeroException.class)
                .hasMessage("This hero is already registered!");

    }


    @Test
    public void getHero_byId_ReturnsHero(){
        when(heroRepository.findById(heroId)).thenReturn(Optional.of(hero));

        Hero sut = heroService.getById(heroId);

        assertThat(sut).isEqualTo(hero);
        assertThat(sut.getName()).isEqualTo(hero.getName());
        assertThat(sut.getHeroCode()).isEqualTo(hero.getHeroCode());
        verify(heroRepository).findById(heroId);
    }

    @Test
    public void getHero_byId_ReturnsHeroNotFoundException(){
        when(heroRepository.findById(heroId)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> heroService.getById(heroId))
                .isInstanceOf(HeroNotFoundException.class)
                .hasMessage("The hero you requested was not found");
    }

    @Test
    public void updateHero_WithValidData_ReturnHeroUpdated(){
       when(heroRepository.findByHeroCode(hero.getHeroCode())).thenReturn(Optional.of(hero));
       when(heroRepository.save(any(Hero.class))).thenReturn(hero);

        Optional <Hero> updatedHero = heroService.updateHero(hero.getHeroCode(), heroRequestDTO);

        assertThat(updatedHero.get().getName()).isEqualTo("Bucky Barnes");
        assertThat(updatedHero.get().getAge()).isEqualTo(100);

        verify(heroRepository,times(1)).save(hero);

    }


    @Test
    public void updateHero_WithInvalidData_ThrowsException(){
        when(heroRepository.findByHeroCode(hero.getHeroCode())).thenReturn(Optional.empty());

        assertThatThrownBy(()->heroService.updateHero(hero.getHeroCode(),heroRequestDTO))
                .isInstanceOf(RuntimeException.class);
    }


    @Test
    public void deleteHero_byId_ReturnsNoContent(){
        when(heroRepository.findById(heroId)).thenReturn(Optional.of(hero));
        doNothing().when(heroRepository).delete(hero);

        heroService.deleteHero(heroId);


        verify(heroRepository).findById(heroId);
        verify(heroRepository).delete(hero);
        verify(heroRepository,times(1)).delete(hero);
    }

    @Test
    public void deleteHero_ById_throwsHeroNotFoundException(){
        when(heroRepository.findById(heroId)).thenReturn(Optional.empty());


        assertThatThrownBy(()->heroService.deleteHero(heroId))
                .isInstanceOf(HeroNotFoundException.class)
                .hasMessage("The hero you requested was not found");

    }

    @Test
    public void getHero_ByHeroCode_ReturnsHero(){
        when(heroRepository.findByHeroCode(hero.getHeroCode())).thenReturn(Optional.of(hero));

        Optional <HeroResponseDTO> sut = heroService.getHeroByCode(hero.getHeroCode());

        assertThat(sut.get()).isEqualTo(heroResponseDTO);
        assertThat(sut).isPresent();
    }

    @Test
    public void getHero_ByHeroCode_ReturnsHeroNotFoundException(){
        when(heroRepository.findByHeroCode(hero.getHeroCode())).thenReturn(Optional.empty());

        assertThatThrownBy(()->heroService.getHeroByCode(hero.getHeroCode()))
                .isInstanceOf(HeroNotFoundException.class)
                .hasMessage("The hero you requested was not found");
    }

    @Test
    public void listHeroes_ByAgentCode_ReturnsListOfHeroes(){

        when(heroRepository.findByAgent_AgentCode(agent.getAgentCode())).thenReturn(List.of(hero));

        List <HeroResponseDTO> heroesList = heroService.getHeroesByAgent(agent.getAgentCode());

        assertThat(heroesList).isNotEmpty();
        assertThat(heroesList).hasSize(1);
        assertThat(heroesList.get(0)).isEqualTo(heroResponseDTO);

    }

    @Test
    public void listHeroes_ByAgentCode_ReturnsEmptyList(){
        when(heroRepository.findByAgent_AgentCode(agent.getAgentCode())).thenReturn(Collections.emptyList());

        List <HeroResponseDTO> heroesList = heroService.getHeroesByAgent(agent.getAgentCode());

        assertThat(heroesList).isEmpty();
    }

    @Test
    public void listAllHeroes_ReturnsListOfHeroes(){
        when(heroRepository.findAll()).thenReturn(List.of(hero));

        List <HeroResponseDTO> heroesList = heroService.getAllHeroes();

        assertThat(heroesList).isNotEmpty();
        assertThat(heroesList).hasSize(1);
        assertThat(heroesList.get(0)).isEqualTo(heroResponseDTO);
    }

    @Test
    public void listAllHeroes_ReturnsEmptyList(){
        when(heroRepository.findAll()).thenReturn(Collections.emptyList());

        List <HeroResponseDTO> heroesList = heroService.getAllHeroes();

        assertThat(heroesList).isEmpty();
    }

}
