package ru.advisio.core.exceptions;

import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.base.AdvisioBaseException;

import java.util.Objects;
import java.util.UUID;

public class AdvisioAccountExistException extends AdvisioBaseException {

    public AdvisioAccountExistException(String cname){
        super(String.format("Company with cname %s is existed", cname));
    }

}
