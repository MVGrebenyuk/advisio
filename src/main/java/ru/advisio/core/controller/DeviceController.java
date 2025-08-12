package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.dto.device.DeviceDto;
import ru.advisio.core.dto.device.DeviceRegisterResponseDto;
import ru.advisio.core.dto.device.LinkDeviceDto;
import ru.advisio.core.dto.device.UnlinkDeviceDto;
import ru.advisio.core.services.DeviceService;

import java.util.List;

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

    @CompanyManager
    @PutMapping("/{cname}/link/sp")
    @Operation(description = "Привязать устройство к торговой точке")
    public void linkDeviceToSalePoint(@PathVariable String cname, @RequestBody LinkDeviceDto linkDeviceDto){
        deviceService.linkDevice(linkDeviceDto);
    }

    @CompanyManager
    @PutMapping("/{cname}/link")
    @Operation(description = "Привязать устройстуво к компании")
    public void linkDevice(@PathVariable String cname, @RequestParam String serial){
        deviceService.linkDevice(cname, serial);
    }

    @CompanyManager
    @PutMapping("/{cname}/unlink")
    @Operation(description = "Привязать устройстуво к компании")
    public void linkDevice(@PathVariable String cname, UnlinkDeviceDto unlinkDeviceDto){
        deviceService.removeFromSalesPoint(unlinkDeviceDto);
    }

    @CompanyManager
    @GetMapping("/{cname}/all")
    @Operation(description = "Получить все девайсы компании")
    public List<DeviceDto> getAllDevices(@PathVariable String cname){
        return deviceService.getAllDevices(cname);
    }

    @CompanyManager
    @PutMapping("/tag/{deviceId}/add")
    @Operation(description = "Изменить тег устройства")
    public boolean changeTag(@PathVariable String deviceId, @RequestParam String tagId){
        return deviceService.setTag(deviceId, tagId);
    }
}
