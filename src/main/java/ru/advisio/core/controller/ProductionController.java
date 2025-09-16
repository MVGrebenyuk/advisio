package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.dto.crm.AdditionalParams;
import ru.advisio.core.dto.crm.CrmProductData;
import ru.advisio.core.dto.products.ProductsCreateRequest;
import ru.advisio.core.services.ProductsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "PRODUCTS API", description = "Контроллер для получение продуктов для комании")
@RequestMapping("/{cname}/products")
public class ProductionController {

    private final ProductsService productsService;

    @CompanyManager
    @GetMapping()
    @Operation(description = "Получение продуктов для генерации изображения по конкретной crm")
    public List<CrmProductData> getProductsByCrmAndCompany(@PathVariable String cname, @RequestParam String crmId){
        return productsService.getProducts(cname, crmId);
    }

    @CompanyManager
    @PostMapping("/{crmId}/local")
    @Operation(description = "Создание продуктов для локальной crm")
    public List<CrmProductData> createLocalProducts(@PathVariable String cname,
                                                     @PathVariable String crmId,
                                                     @RequestBody ProductsCreateRequest request){
        return productsService.createProducts(cname, crmId, request);
    }

    @CompanyManager
    @PutMapping("/{crmId}/local")
    @Operation(description = "Обновление продуктов для локальной crm")
    public List<CrmProductData> updateLocalProducts(@PathVariable String cname,
                                                     @PathVariable String crmId,
                                                     @RequestBody ProductsCreateRequest request){
        return productsService.updateProducts(cname, crmId, request);
    }

    @CompanyManager
    @DeleteMapping("/{crmId}/local")
    @Operation(description = "Удаленеи продукта по id из данных локлаьной CRM")
    public boolean deleteLocalProducts(@PathVariable String cname, @PathVariable String crmId,
                                                    @RequestParam UUID productId){
        return productsService.deleteProduct(crmId, productId);
    }

    @CompanyManager
    @DeleteMapping("/{crmId}/local/{productId}")
    @Operation(description = "Удаление дополнительных параметров продукта по id из данных локлаьной CRM")
    public boolean deleteLocalProducts(@PathVariable String cname, @PathVariable String crmId,
                                       @PathVariable UUID productId, @RequestParam UUID paramId){
        return productsService.deleteAdditionalParam(crmId, paramId);
    }

    @Deprecated
    @GetMapping("/mock")
    @Operation(description = "Заглушка для теста")
    public List<CrmProductData> getProductionByCrmAndCompany(@PathVariable String cname){
        return List.of(CrmProductData.builder()
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
                CrmProductData.builder()
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
