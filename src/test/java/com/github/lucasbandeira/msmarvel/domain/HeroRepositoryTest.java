package com.github.lucasbandeira.msmarvel.domain;

import static com.github.lucasbandeira.msmarvel.common.HeroConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.lucasbandeira.msmarvel.infra.repository.AgentRepository;
import com.github.lucasbandeira.msmarvel.infra.repository.HeroRepository;
import com.github.lucasbandeira.msmarvel.model.Agent;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroResponseDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class HeroRepositoryTest {

    private Agent agent;
    private Hero hero;
    private HeroRequestDTO heroRequestDTO;
    private HeroResponseDTO heroResponseDTO;
    private final UUID heroId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");



    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp(){
        agent = createAgent();
        hero = createHero();
        heroRequestDTO = createHeroRequestDTO();
        heroResponseDTO = createHeroResponseDTO();
    }

    @Test
    public void findHero_byHeroCode_ReturnsHero(){
        Hero savedHero = entityManager.persistFlushFind(hero);
        Optional <Hero> heroOptional = heroRepository.findByHeroCode(hero.getHeroCode());

        assertThat(heroOptional).isNotEmpty();
        assertThat(heroOptional.get()).isEqualTo(savedHero);
    }

    @Test
    public void findHero_byHeroCode_ReturnsEmpty(){
        Optional <Hero> heroOptional = heroRepository.findByHeroCode(hero.getHeroCode());

        assertThat(heroOptional).isEmpty();
    }

    @Test
    public void findHero_ByAgent_ReturnListOfHeroes(){
        Agent savedAgent = entityManager.persistFlushFind(agent);
        hero.setAgent(savedAgent);
        entityManager.persist(hero);
        List <Hero> heroList = heroRepository.findByAgent_AgentCode(savedAgent.getAgentCode());

        assertThat(heroList).isNotEmpty();
        assertThat(heroList).hasSize(1);
        assertThat(heroList.get(0)).isEqualTo(hero);
    }

    @Test
    public void findHero_ByAgent_ReturnEmptyList(){
        List <Hero> heroList = heroRepository.findByAgent_AgentCode(agent.getAgentCode());

        assertThat(heroList).isEmpty();
    }

}
