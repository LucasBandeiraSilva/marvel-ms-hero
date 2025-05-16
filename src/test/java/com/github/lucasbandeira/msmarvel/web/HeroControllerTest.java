package com.github.lucasbandeira.msmarvel.web;

import com.github.lucasbandeira.msmarvel.common.HeroConstants;
import com.github.lucasbandeira.msmarvel.exception.DuplicateHeroException;
import com.github.lucasbandeira.msmarvel.exception.HeroNotFoundException;
import com.github.lucasbandeira.msmarvel.model.Agent;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;
import com.github.lucasbandeira.msmarvel.resources.HeroService;
import com.github.lucasbandeira.msmarvel.validator.HeroValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class HeroControllerTest {

    private final UUID heroId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private HeroRequestDTO validHeroRequestDTO;
    private HeroRequestDTO invalidHeroRequestDTO;
    private HeroResponseDTO heroResponseDTO;
    private Hero hero;
    private Hero invalidHero;
    private Agent agent;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private HeroService heroService;

    @MockitoBean
    private HeroValidator validator;


    @BeforeEach
    void setUp() {
        validHeroRequestDTO = HeroConstants.createHeroRequestDTO();
        invalidHeroRequestDTO = HeroConstants.createInvalidHeroRequestDTO();
        heroResponseDTO = HeroConstants.createHeroResponseDTO();
        hero = HeroConstants.createHero();
        agent = HeroConstants.createAgent();
        invalidHero = HeroConstants.createInvalidHero();
    }

    @Test
    void findAllHeroes_returnsListOfHeroes() {
        when(heroService.getAllHeroes()).thenReturn(List.of(heroResponseDTO));

        List <HeroResponseDTO> list = webTestClient.get()
                .uri("/hero")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(HeroResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(list).isNotEmpty();
        assertThat(list.get(0)).isEqualTo(heroResponseDTO);
        assertThat(list).hasSize(1);

        verify(heroService, times(1)).getAllHeroes();
    }

    @Test
    void findAllHeroes_returnsEmptyList() {
        when(heroService.getAllHeroes()).thenReturn(Collections.emptyList());

        webTestClient.get()
                .uri("/hero")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(HeroResponseDTO.class).hasSize(0);
    }

    @Test
    void listHeroes_ByAgentCode_ReturnsListOfHeroes() {
        when(heroService.getHeroesByAgent(agent.getAgentCode())).thenReturn(List.of(heroResponseDTO));

        List <HeroResponseDTO> list = webTestClient.get().uri("/hero/by-agent/" + agent.getAgentCode())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(HeroResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(list).isNotEmpty();
        assertThat(list.get(0)).isEqualTo(heroResponseDTO);
        assertThat(list).hasSize(1);

        verify(heroService, times(1)).getHeroesByAgent(agent.getAgentCode());

    }

    @Test
    void listHeroes_ByAgentCode_ReturnsEmptyList() {
        when(heroService.getHeroesByAgent(any(String.class))).thenReturn(Collections.emptyList());

        webTestClient.get().uri("/hero/by-agent/" + agent.getAgentCode())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBodyList(HeroResponseDTO.class).hasSize(0);
    }


    @Test
    void createHero_withValidData_ReturnsCreated() {

        when(heroService.saveHero(Hero.fromDTO(validHeroRequestDTO))).thenReturn(hero);

        webTestClient.post().uri("/hero").bodyValue(validHeroRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader().value("Location", location -> assertThat(location).contains("/hero/"));

        verify(heroService, times(1)).saveHero(any(Hero.class));
    }


    @Test
    void createHero_withInvalidData_ReturnsDuplicateHeroException() {
        when(heroService.saveHero(invalidHero)).thenThrow(new DuplicateHeroException("This hero is already registered!"));
        webTestClient.post()
                .uri("/hero")
                .bodyValue(invalidHero)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody).contains("This hero is already registered!");
                });

    }

    @Test
    void createHero_withInvalidData_ReturnsUnprocessableEntity() {
        webTestClient.post()
                .uri("/hero")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidHeroRequestDTO)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void getHero_byId_ReturnsHero() {
        when(heroService.getById(heroId)).thenReturn(hero);

        HeroResponseDTO sut = webTestClient.get()
                .uri("/hero/" + heroId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HeroResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(sut).isNotNull();
        assertThat(sut.heroCode()).isEqualTo(heroResponseDTO.heroCode());
        assertThat(sut.age()).isEqualTo(heroResponseDTO.age());
        assertThat(sut.characteristics()).isEqualTo(heroResponseDTO.characteristics());
        assertThat(sut.name()).isEqualTo(heroResponseDTO.name());
        assertThat(sut.skills()).isEqualTo(heroResponseDTO.skills());

        verify(heroService, times(1)).getById(heroId);
    }

    @Test
    void getHero_byId_ReturnsHeroNotFoundException() {

        when(heroService.getById(heroId)).thenThrow(new HeroNotFoundException("The hero you requested was not found"));

        webTestClient.get()
                .uri("/hero/" + heroId)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody).contains("The hero you requested was not found");
                });

    }

    @Test
    void updateHero_withValidData_returnsHeroUpdated() {
        when(heroService.updateHero(hero.getHeroCode(), validHeroRequestDTO)).thenReturn(Optional.of(hero));

        webTestClient.put()
                .uri("/hero/" + hero.getHeroCode())
                .bodyValue(validHeroRequestDTO)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void updateHero_withInvalidData_returnsHeroNotFoundException() {

        when(heroService.updateHero(hero.getHeroCode(), validHeroRequestDTO)).thenThrow(new HeroNotFoundException("The hero you requested was not found!"));

        webTestClient.put().uri("/hero/" + hero.getHeroCode())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(validHeroRequestDTO)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(response -> {
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody).contains("The hero you requested was not found!");
                });


    }

    @Test
    void updateHero_withInvalidData_ReturnsUnprocessableEntity() {
        webTestClient.put().uri("/hero/"+hero.getHeroCode())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidHeroRequestDTO)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void updateHero_withInvalidData_ReturnsDuplicateHeroException(){
        when(heroService.updateHero(hero.getHeroCode(), validHeroRequestDTO)).thenThrow(new DuplicateHeroException("This hero is already registered!"));

        webTestClient.put().uri("/hero/"+hero.getHeroCode())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(validHeroRequestDTO)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .consumeWith(response->{
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody).contains("This hero is already registered!");
                });

    }


    @Test
    void deleteHero_byId_ReturnsNoContent() {
        doNothing().when(heroService).deleteHero(heroId);

        webTestClient.delete()
                .uri("/hero/" + heroId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void deleteHero_byId_ReturnsHeroNotFoundException(){
        doThrow(new HeroNotFoundException("The hero you requested was not found")).when(heroService).deleteHero(heroId);

        webTestClient.delete().uri("/hero/"+heroId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getHero_ByHeroCode_ReturnsHero() {
        when(heroService.getHeroByCode(hero.getHeroCode())).thenReturn(Optional.of(heroResponseDTO));

        HeroResponseDTO sut = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/hero").queryParam("hero-code", hero.getHeroCode()).build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HeroResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(sut).isNotNull();
    }

    @Test
    void getHero_ByHeroCode_ReturnsHeroNotFoundException(){
        when(heroService.getHeroByCode(hero.getHeroCode())).thenThrow(new HeroNotFoundException("The hero you requested was not found"));
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/hero").queryParam("hero-code",hero.getHeroCode()).build())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(response->{
                    String responseBody = new String(response.getResponseBody());
                    assertThat(responseBody).contains("The hero you requested was not found");
                });
    }

}
