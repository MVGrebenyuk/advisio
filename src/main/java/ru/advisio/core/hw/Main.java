package ru.advisio.core.hw;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> someArgs = new ArrayList<>();
        someArgs.add("command1");
        someArgs.add("command2");
        someArgs.add("command3");

        Integer i = 5;
        Integer j = 0;

        someArgs.forEach(str -> {
            try {
                if (!str.equals("command3")) {
                    System.out.println("ToDo some logic");
                } else {
                    throw new WrongCommandException();
                }
            } catch (WrongCommandException e) {

            }

            System.out.println("This was command: " + str);
        });

        devideByZero(i, j);
    }

    public static void devideByZero(Integer i, Integer j){
        var result = i / j;
    }

    public static void someMethod(String str) throws EntityNotFoundException{
        if (!str.equals("command3")) {
            System.out.println("ToDo some logic");
        } else {
            throw new EntityNotFoundException();
        }
    }

}
