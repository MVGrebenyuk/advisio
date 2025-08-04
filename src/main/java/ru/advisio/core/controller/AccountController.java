package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.dto.account.AccountRegisterDto;
import ru.advisio.core.dto.account.AccountResponseDto;
import ru.advisio.core.services.AccountService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ACCOUNT API", description = "Контроллер регистрации и управления учетными записями")
@RequestMapping("/account")
public class AccountController {

    private final AccountService service;

    @PostMapping("/register")
    public AccountResponseDto register(@RequestBody AccountRegisterDto requestDto){
        return service.register(requestDto);
    }

    public AccountResponseDto getAccountByEmailOrPhone(String requestDto){
        return null;
    }

}
