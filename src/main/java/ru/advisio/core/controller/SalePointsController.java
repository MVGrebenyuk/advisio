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
import ru.advisio.core.aop.CompanyAdmin;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.device.DeviceDto;
import ru.advisio.core.dto.salepoints.CreateSalePointDto;
import ru.advisio.core.dto.salepoints.SalePointDto;
import ru.advisio.core.dto.salepoints.UpdateSalePointDto;
import ru.advisio.core.entity.Device;
import ru.advisio.core.entity.Image;
import ru.advisio.core.services.SalePointsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{cname}/sp")
@Tag(name = "SALES POINT API", description = "Контроллер для обработки запросов, связанных с изображениями")

public class SalePointsController {

    private final SalePointsService service;

    @CompanyAdmin
    @PostMapping("/create")
    @Operation(description = "Создание торговой точки (адимн права)")
    public SalePointDto createSalesPoint(@PathVariable String cname, CreateSalePointDto salePointDto){
       return service.createSalesPoint(cname, salePointDto);
    }

    @CompanyObserver
    @GetMapping("/get")
    @Operation(description = "Получение торговой точки по id")
    public SalePointDto getSalePoint(@PathVariable String cname, @RequestParam String spId){
        return service.getSalesPoint(cname, spId);
    }

    @CompanyObserver
    @GetMapping("/all")
    @Operation(description = "Получение всех торговых точек компании")
    public List<SalePointDto> getAllSalePoint(@PathVariable String cname){
        return service.getAllSalesPointsByCname(cname);
    }

    @CompanyManager
    @PutMapping("/{spId}/edit")
    @Operation(description = "Редактирование торговой точки (админ\\менеджер)")
    public SalePointDto getAllSalePoint(@PathVariable String cname, @PathVariable String spId, @RequestBody UpdateSalePointDto dto){
        return service.editSalePoint(cname, spId, dto);
    }

    @CompanyObserver
    @GetMapping("/{spId}/images/all")
    @Operation(description = "Получение изображений торговой точки")
    public List<Image> getAllImagesSalePoint(@PathVariable String cname, @PathVariable String spId){
        return service.getAllImagesBySp(cname, spId);
    }

    @CompanyObserver
    @GetMapping("/{spId}/devices/all")
    @Operation(description = "Получение девайсов торговой точки")
    public List<DeviceDto> getAllDevicesSalePoint(@PathVariable String cname, @PathVariable String spId){
        return service.getAllDevicesBySp(cname, spId);
    }
}
