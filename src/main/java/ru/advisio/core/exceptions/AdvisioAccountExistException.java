package ru.advisio.core.exceptions;

import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.base.AdvisioBaseException;

import java.util.Objects;
import java.util.UUID;

public class AdvisioAccountExistException extends AdvisioBaseException {

    public AdvisioAccountExistException(String email, String phone){
        super(String.format("Account with email: %s or phone: %s is existed", email, phone));
    }

}
