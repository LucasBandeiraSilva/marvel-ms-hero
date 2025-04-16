package com.github.lucasbandeira.msmarvel.model.dto;

import java.util.List;

public record ErrorResponse(int status, String message, List<ApiFieldError>errors) {
}
