package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.template.TemplateDto;
import ru.advisio.core.services.TemplateService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TEMPLATE API", description = "Контроллер для обработки запросов, связанных с шаблонами")
@RequestMapping("/{cname}/templates")
public class TemplateController {

    private final TemplateService service;

    @CompanyManager
    @PostMapping("/upload")
    @Operation(description = "Загрузка шаблона медиафайла")
    public TemplateDto uploadTemplate(@PathVariable String cname, @RequestBody MultipartFile file){
        return service.uploadTemplate(cname, file);
    }

    @CompanyObserver
    @GetMapping("/all")
    @Operation(description = "Получение всех шаблонов медиафайлов")
    public Page<TemplateDto> getAllTemplates(@PathVariable String cname, @RequestParam(required = false) Integer pageNumber,
                                             @RequestParam(required = false) Integer pageSize){
        return service.getAllTemplates(cname, pageNumber, pageSize);
    }

    @CompanyObserver
    @GetMapping()
    @Operation(description = "Получение шаблона по id")
    public TemplateDto getTemplateById(@PathVariable String cname, @RequestParam String id){
        return service.getTempalteById(id);
    }

    @CompanyManager
    @DeleteMapping("/delete")
    @Operation(description = "Удаление шаблона медиафайла")
    public Boolean uploadTemplate(@PathVariable String cname, @RequestParam String id){
        return service.deleteTemplate(id);
    }

    @CompanyManager
    @PutMapping("/update")
    @Operation(description = "Переименование шаблона")
    public TemplateDto uploadTemplate(@PathVariable String cname, @RequestParam String id, @RequestParam String name){
        return service.updateTemplate(id, name);
    }

}
