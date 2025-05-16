package com.github.lucasbandeira.msmarvel.domain;

import static com.github.lucasbandeira.msmarvel.common.HeroConstants.createAgent;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.lucasbandeira.msmarvel.infra.repository.AgentRepository;
import com.github.lucasbandeira.msmarvel.model.Agent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class AgentRepositoryTest {

    private Agent agent;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp(){
        agent = createAgent();
    }

    @Test
    public void findAgent_ByAgentCode_ReturnsAgent(){
        Agent savedAgent = testEntityManager.persistFlushFind(agent);

        Optional <Agent> agentOptional = agentRepository.findByAgentCode(savedAgent.getAgentCode());

        assertThat(agentOptional).isNotEmpty();
        assertThat(agentOptional.get()).isEqualTo(savedAgent);

    }

    @Test
    public void findAgent_ByAgentCode_ReturnsEmpty(){
        Optional <Agent> agentOptional = agentRepository.findByAgentCode(agent.getAgentCode());
        assertThat(agentOptional).isEmpty();

    }




}
