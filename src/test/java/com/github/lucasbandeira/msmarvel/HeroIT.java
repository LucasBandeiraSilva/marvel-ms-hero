package com.github.lucasbandeira.msmarvel;

import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static com.github.lucasbandeira.msmarvel.common.HeroConstants.*;
import static com.github.lucasbandeira.msmarvel.common.HeroConstants.createHeroResponseDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("it")
@Sql(scripts = {"/import_data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeroIT {


    private HeroRequestDTO validHeroRequestDTO;
    private HeroRequestDTO invalidHeroRequestDTO;
    private HeroResponseDTO heroResponseDTO;
    private Hero hero;



    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(){
        hero = createHero();
        validHeroRequestDTO = createHeroRequestDTO();
        heroResponseDTO = createHeroResponseDTO();
        invalidHeroRequestDTO = createInvalidHeroRequestDTO();
    }


    @Test
    void listAllHeroes_ReturnsListOfHeroes(){
        List <HeroResponseDTO> list = webTestClient
                .get()
                .uri("/hero")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(HeroResponseDTO.class).hasSize(3)
                .returnResult().getResponseBody();

        assertThat(list).isNotEmpty();

    }


    @Test
    void listHeroesByAgentId_ReturnsHeroesList(){
        List <HeroResponseDTO> list = webTestClient.get()
                .uri("/hero/by-agent/{agentCode}","Shield-001" )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(HeroResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(list).isNotEmpty();
    }
    @Test
    void listHeroesByAgentId_ReturnsEmptyList(){
        webTestClient.get()
                .uri("/hero/by-agent/{agentCode}","Shield-002")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    void createHero_ReturnsCreated(){
        webTestClient.post()
                .uri("/hero")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hero)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader().value("location",location -> assertThat(location).contains("/hero/"));
    }


    @Test
    void createHero_ReturnsUnprocessableEntity() {
        webTestClient.post()
                .uri("/hero")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidHeroRequestDTO)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void getHeroById_ReturnsHero(){
        HeroResponseDTO sut = webTestClient.get()
                .uri("/hero/{id}", "187bc50e-b503-4034-9ba1-b1dfd2da3b25")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HeroResponseDTO.class)
                .returnResult().getResponseBody();

        assertThat(sut.heroCode()).isNotNull();

    }

    @Test
    void getHeroById_ReturnsHeroHeroNotFound(){
        webTestClient.get()
                .uri("/hero/{id}",UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }


    @Test
    void updateHero_returnsHeroUpdated(){
        webTestClient.put()
                .uri("/hero/{heroCode}",validHeroRequestDTO.heroCode())
                .bodyValue(validHeroRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();

        webTestClient.get()
                .uri("/hero/{id}","187bc50e-b503-4034-9ba1-b1dfd2da3b25")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(validHeroRequestDTO.name())
                .jsonPath("$.skills").isArray()
                .jsonPath("$.skills[0]").isEqualTo(validHeroRequestDTO.skills().get(0))
                .jsonPath("$.age").isEqualTo(validHeroRequestDTO.age())
                .jsonPath("$.characteristics").isArray();
    }

    @Test
    void updateHero_ReturnsUnprocessableEntity(){
        webTestClient.put()
                .uri("/hero/{heroCode}",invalidHeroRequestDTO.heroCode())
                .bodyValue(invalidHeroRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void deleteHero_ReturnsNoContent(){
        webTestClient.delete()
                .uri("/hero/{id}","82fde130-62a7-4270-8028-ba7b18022f25")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();

        webTestClient.get()
                .uri("/hero/{id}","82fde130-62a7-4270-8028-ba7b18022f25")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteHero_ReturnNotFound(){
        String invalidId = "9a714f75-819a-4cc8-b13b-9469ac7a2c71";
        webTestClient.delete()
                .uri("/hero/{id}",invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getByHeroCode_ReturnsHero(){
        webTestClient
                .get()
                .uri("/hero/code/{heroCode}",validHeroRequestDTO.heroCode())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HeroResponseDTO.class)
                .value(hero ->{
                   assertThat("Winter Soldier").isEqualTo(hero.name());
                   assertThat(38).isEqualTo(hero.age());
                });
    }
}
