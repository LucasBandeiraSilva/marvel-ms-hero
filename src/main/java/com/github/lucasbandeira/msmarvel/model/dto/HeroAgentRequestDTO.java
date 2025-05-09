package com.github.lucasbandeira.msmarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record HeroAgentRequestDTO( HeroRequestDTO hero,
                                   AgentRequestDTO agent) {
}
