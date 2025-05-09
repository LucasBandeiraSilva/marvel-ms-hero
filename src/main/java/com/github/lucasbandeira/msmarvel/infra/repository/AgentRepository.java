package com.github.lucasbandeira.msmarvel.infra.repository;

import com.github.lucasbandeira.msmarvel.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {

    Optional<Agent> findByAgentCode( String agentCode);
}
