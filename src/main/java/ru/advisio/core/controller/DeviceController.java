package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.dto.DeviceRegisterResponseDto;
import ru.advisio.core.services.DeviceService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "DEVICE API", description = "Контроллер регистрации устройств")
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/register")
    @Operation(description = "Регистрация девайса при первом подключении")
    public DeviceRegisterResponseDto register(@RequestParam(name = "serial") String serial){
        log.info("Устройство с id {} пытается зарегистрироваться", serial);
        return deviceService.registration(serial);
    }

    @GetMapping("/is_register")
    @Operation(description = "Проверка, зарегистрирован ли такой девайс")
    public Boolean testRegister(@RequestParam(name = "id") String id){
        return deviceService.isRegister(id);
    }
}
