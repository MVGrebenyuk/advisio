package ru.advisio.core.controller;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.account.CompanyRegisterDto;
import ru.advisio.core.dto.account.CompanyResponseDto;
import ru.advisio.core.services.CompanyService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "COMPANY API", description = "Контроллер регистрации и управления компаниями")
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService service;

    @PostMapping("/register")
    public CompanyResponseDto register(Principal principal, @RequestBody CompanyRegisterDto requestDto){
        return service.register(principal, requestDto);
    }

    @GetMapping("/my")
    public Set<CompanyResponseDto> getUserCompanyes(){
        return service.getUserCompanyes();
    }

    @PutMapping()
    public CompanyResponseDto getAccountByEmailOrPhone(String requestDto){

        return null;
    }

}
