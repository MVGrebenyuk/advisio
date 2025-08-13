package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.media.MediaDto;
import ru.advisio.core.services.MediaService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MEDIA API", description = "Контроллер для обработки запросов, связанных с медиа")
@RequestMapping("/{cname}/media")
public class MediaController {

    private final MediaService service;

    @CompanyObserver
    @GetMapping("/all/active")
    @Operation(description = "Получение всех активных медиафайлов")
    public Page<MediaDto> getAllActiveTemplates(@PathVariable String cname, @RequestParam(required = false) Integer pageNumber,
                                          @RequestParam(required = false) Integer pageSize){
        return service.getAllActiveMedia(cname, pageNumber, pageSize);
    }

    @CompanyObserver
    @GetMapping("/all/created")
    @Operation(description = "Получение всех медиафайлов")
    public Page<MediaDto> getAllTemplates(@PathVariable String cname, @RequestParam(required = false) Integer pageNumber,
                                          @RequestParam(required = false) Integer pageSize){
        return service.getAllCreatedMedia(cname, pageNumber, pageSize);
    }
}
