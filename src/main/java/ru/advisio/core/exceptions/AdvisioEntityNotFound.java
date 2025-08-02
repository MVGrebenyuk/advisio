package ru.advisio.core.exceptions;

import ru.advisio.core.enums.EnType;

import java.util.UUID;

public class AdvisioEntityNotFound extends RuntimeException{

    public AdvisioEntityNotFound(EnType type, String id){
        super(type.name() + "not found with id " + id);
    }

    public AdvisioEntityNotFound(EnType type, UUID id){
        super(type.name() + "not found with id " + id);
    }

}
