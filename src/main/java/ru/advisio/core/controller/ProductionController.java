package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.dto.crm.AdditionalParams;
import ru.advisio.core.dto.crm.CrmData;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "PRODUCTS API", description = "Контроллер для получение продуктов для комании")
@RequestMapping("/{cname}/products")
public class ProductionController {

    @GetMapping()
    public List<CrmData> getProductionByCrmAndCompany(@PathVariable String cname){
        return List.of(CrmData.builder()
                .id(UUID.randomUUID().toString())
                .techName("св.п. Три Быка. розн")
                .saleName("Три Быка")
                .additional(List.of(AdditionalParams.builder()
                                .id(UUID.randomUUID().toString())
                                .techName("vol")
                                .name("Объем")
                                .value("0.5")
                        .build(),
                        AdditionalParams.builder()
                                .id(UUID.randomUUID().toString())
                                .techName("kr")
                                .name("Крепость")
                                .value("4%")
                                .build()))
                .build(),
                CrmData.builder()
                        .id(UUID.randomUUID().toString())
                        .techName("темн.п. Наглецы. розн")
                        .saleName("Наглецы из Воронежа (темное)")
                        .additional(List.of(AdditionalParams.builder()
                                        .id(UUID.randomUUID().toString())
                                        .techName("vlm")
                                        .name("Объем")
                                        .value("0.5")
                                                .techParams(List.of(AdditionalParams.builder()
                                                        .id(UUID.randomUUID().toString())
                                                        .techName("tara")
                                                        .name("Отпускная тара")
                                                        .value("Стекло")
                                                        .build()))
                                        .build(),
                                AdditionalParams.builder()
                                        .id(UUID.randomUUID().toString())
                                        .techName("krq")
                                        .name("Крепость")
                                        .value("6%")
                                        .build(),
                                AdditionalParams.builder()
                                        .id(UUID.randomUUID().toString())
                                        .techName("maintainer")
                                        .name("Производитель")
                                        .value("BREWLOCK")
                                        .build()))
                        .build()
                );
    }

}
