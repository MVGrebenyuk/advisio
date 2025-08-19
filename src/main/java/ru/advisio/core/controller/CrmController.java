package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyAdmin;
import ru.advisio.core.dto.crm.ConnectionCrmDto;
import ru.advisio.core.dto.crm.CrmDto;
import ru.advisio.core.services.CrmService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CRM API", description = "Контроллер для работы с CRM компании")
@RequestMapping("/{cname}/crm")
public class CrmController {

    private final CrmService crmService;

    @CompanyAdmin
    @PostMapping("/create")
    @Operation(description = "Создать подключение к Crm \\ локальную Crm")
    public CrmDto createCrm(@PathVariable String cname, @RequestBody CrmDto crmDto){
        return crmService.create(cname, crmDto);
    }

    @CompanyAdmin
    @PostMapping("/test")
    @Operation(description = "Протестировать подключение к crm до создания")
    public CrmDto testCrm(@PathVariable String cname, @RequestBody CrmDto crmDto){
        return crmService.test(cname, crmDto);
    }

    @CompanyAdmin
    @PostMapping("/{crmId}/update")
    @Operation(description = "Обновить данные подключения к CRM")
    public CrmDto updateCrmConnections(@PathVariable String cname, @RequestBody ConnectionCrmDto crmDto){
        return crmService.updateConnection(cname, crmDto);
    }

}
