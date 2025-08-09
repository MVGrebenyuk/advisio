package ru.advisio.core.controller;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.account.CompanyRegisterDto;
import ru.advisio.core.dto.account.CompanyResponseDto;
import ru.advisio.core.entity.Device;
import ru.advisio.core.entity.Image;
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
    @Schema(description = "Зарегистрировать новую компанию")
    public CompanyResponseDto register(Principal principal, @RequestBody CompanyRegisterDto requestDto){
        return service.register(principal, requestDto);
    }

    @GetMapping("/my")
    @Schema(description = "Получить список моих компаний")
    public Set<CompanyResponseDto> getUserCompanyes(){
        return service.getUserCompanyes();
    }

    @CompanyManager
    @GetMapping("/{cname}/images")
    @Schema(description = "Получить медиафайлы компании со всех точек")
    public List<Image> getCompanyImages(@PathVariable String cname){
        return service.getCompanyImages(cname);
    }

    @CompanyManager
    @GetMapping("/{cname}/devices")
    @Schema(description = "Получить все девайсы компании со всех точек")
    public List<Device> getCompanyDevices(@PathVariable String cname){
        return service.getCompanyDevices(cname);
    }
}
