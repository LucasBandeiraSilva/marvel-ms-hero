package com.github.lucasbandeira.msmarvel.model;

import com.github.lucasbandeira.msmarvel.model.dto.AgentRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tb_agent")
@Data
@AllArgsConstructor
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String agentCode;
    private String name;
    private boolean active;


    public Agent( String agentCode, String name, boolean active ) {
        this.agentCode = agentCode;
        this.name = name;
        this.active = active;
    }

    public static Agent fromDTO ( AgentRequestDTO agentRequestDTO ){
        return new Agent(agentRequestDTO.agentCode(),agentRequestDTO.name(),agentRequestDTO.active());
    }
}
