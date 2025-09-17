package ru.advisio.core.controller.media;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.dto.crm.CrmDto;
import ru.advisio.core.dto.crm.CrmProductData;
import ru.advisio.core.dto.editor.ImageDataToSaveDto;
import ru.advisio.core.dto.image.ImageResponseDto;
import ru.advisio.core.dto.template.TemplateDto;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.services.AwsService;
import ru.advisio.core.services.CrmService;
import ru.advisio.core.services.ProductsService;
import ru.advisio.core.services.TemplateService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "EDITOR API", description = "Контроллер для работы с редактором")
@RequestMapping("/{cname}/editor")
public class EditorController {

    private final TemplateService templateService;
    private final AwsService awsService;
    private final ProductsService productsService;
    private final CrmService crmService;

    @CompanyManager
    @PostMapping("/template/upload")
    @Operation(description = "Загрузка изображения из редактора (переходит автоматом в шаблоны)")
    public TemplateDto uploadTemplate(@PathVariable String cname, @RequestBody MultipartFile file){
        return templateService.uploadTemplate(cname, file);
    }

    @CompanyManager
    @PostMapping("/save")
    @Operation(description = "Сохранение сгенерированного изображения")
    public ImageResponseDto uploadGeneratedImage(@PathVariable String cname, @ModelAttribute ImageDataToSaveDto data){
        return awsService.uploadImageFromEditor(data, EnType.COMPANY, cname);
    }

    @CompanyManager
    @GetMapping("/products")
    @Operation(description = "Получение продуктов по crm")
    public List<CrmProductData> getProductsByCrmAndCompany(@PathVariable String cname, @RequestParam String crmId){
        return productsService.getProducts(cname, crmId);
    }

    @CompanyManager
    @GetMapping("/crm/all")
    @Operation(description = "Получить список Crm компании")
    public List<CrmDto> getAlLCrmByCompany(@PathVariable String cname){
        return crmService.getCompanyCrms(cname);
    }

    @CompanyManager
    @GetMapping("/crm")
    @Operation(description = "Получить Crm компании по id")
    public CrmDto getCrmByid(@PathVariable String cname, @RequestParam UUID crmId){
        return crmService.getCompanyCrm(crmId);
    }
}
