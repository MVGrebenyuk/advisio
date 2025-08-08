package ru.advisio.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.advisio.core.dto.account.CompanyRegisterDto;
import ru.advisio.core.dto.account.CompanyResponseDto;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@RequestMapping("/admin_panel")
public class AdminController {

    @PostMapping("/register/for")
    public CompanyResponseDto register(@RequestBody CompanyRegisterDto requestDto){
        return null;
    }

}
