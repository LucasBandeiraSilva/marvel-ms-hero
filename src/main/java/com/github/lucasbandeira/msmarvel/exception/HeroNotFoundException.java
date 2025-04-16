package com.github.lucasbandeira.msmarvel.exception;

public class HeroNotFoundException extends RuntimeException {
    public HeroNotFoundException( String message ) {
        super(message);
    }
}
