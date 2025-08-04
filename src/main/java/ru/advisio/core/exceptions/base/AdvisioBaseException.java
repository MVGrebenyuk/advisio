package ru.advisio.core.exceptions.base;

public class AdvisioBaseException extends RuntimeException{

    public AdvisioBaseException(){
        super();
    }

    public AdvisioBaseException(String message){
        super(message);
    }

}
