package com.github.lucasbandeira.msmarvel.infra.repository.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lucasbandeira.msmarvel.infra.repository.AgentRepository;
import com.github.lucasbandeira.msmarvel.infra.repository.HeroRepository;
import com.github.lucasbandeira.msmarvel.model.Agent;
import com.github.lucasbandeira.msmarvel.model.Hero;
import com.github.lucasbandeira.msmarvel.model.dto.HeroAgentRequestDTO;
import com.github.lucasbandeira.msmarvel.model.dto.HeroRequestDTO;
import com.github.lucasbandeira.msmarvel.resources.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeroRegistrationSubscriber {

    private final AgentRepository agentRepository;
    private final HeroRepository heroRepository;
    private final HeroService heroService;


    @RabbitListener(queues = "${mq.queues.hero-registration}")
    public void receiveHeroRegistrationRequest( @Payload String payload ) {
        try {
            var mapper = new ObjectMapper();
            HeroAgentRequestDTO heroAgentRequestDTO = mapper.readValue(payload, HeroAgentRequestDTO.class);

            Agent agent = agentRepository.findByAgentCode(heroAgentRequestDTO.agent().agentCode()).orElseGet(() -> agentRepository.save(Agent.fromDTO(heroAgentRequestDTO.agent())));


            Hero hero = Hero.fromDTO(heroAgentRequestDTO.hero());
            hero.setAgent(agent);

            heroRepository.save(hero);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "${mq.queues.hero-update}")
    public void handleHeroUpdate( @Payload String payload ) {
        try {
            var mapper = new ObjectMapper();
            HeroRequestDTO heroRequestDTO = mapper.readValue(payload, HeroRequestDTO.class);
            heroService.updateHero(heroRequestDTO.heroCode(), heroRequestDTO);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
