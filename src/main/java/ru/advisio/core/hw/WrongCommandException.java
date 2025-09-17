package ru.advisio.core.hw;

public class WrongCommandException extends RuntimeException{

    private static final String message = "Необрабатываемая комманда";
    public WrongCommandException() {
        super(message);
    }

}
