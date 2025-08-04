package ru.advisio.core.exceptions;

import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.base.AdvisioBaseException;

import java.util.UUID;

public class AdvisioEntityNotFound extends AdvisioBaseException {

    public AdvisioEntityNotFound(EnType type, String id){
        super(type.name() + "not found with id " + id);
    }

    public AdvisioEntityNotFound(EnType type, UUID id){
        super(type.name() + "not found with id " + id);
    }

}
