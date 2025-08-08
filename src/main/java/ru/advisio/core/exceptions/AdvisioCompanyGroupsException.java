package ru.advisio.core.exceptions;

import ru.advisio.core.exceptions.base.AdvisioBaseException;

public class AdvisioCompanyGroupsException extends AdvisioBaseException {
    public AdvisioCompanyGroupsException(String message){
        super(String.format("Failed to create group for company %s", message));
    }

    public AdvisioCompanyGroupsException(String group, String subgroup){
        super(String.format("Failed to create subgroup %s for company %s", group, subgroup));
    }
}
