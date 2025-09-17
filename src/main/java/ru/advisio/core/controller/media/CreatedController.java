package ru.advisio.core.controller.media;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.media.MediaDto;
import ru.advisio.core.entity.Image;
import ru.advisio.core.services.ImageService;
import ru.advisio.core.services.MediaService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CREATED MEDIA API", description = "Контроллер для работы с созданными изображениями")
@RequestMapping("/{cname}/created")
public class CreatedController {

    private final MediaService service;
    private final ImageService imageService;

    @CompanyObserver
    @GetMapping("/all")
    @Operation(description = "Получение всех созданных медиафайлов")
    public Page<MediaDto> getAll(@PathVariable String cname, @RequestParam(required = false) Integer pageNumber,
                                                @RequestParam(required = false) Integer pageSize){
        return service.getAllCreatedMedia(cname, pageNumber, pageSize);
    }

    @CompanyManager
    @GetMapping("/data")
    @Operation(description = "Получить данные изображения (для показа, редактирования)")
    public Image getCreatedById(@PathVariable String cname, @RequestParam String id){
        return imageService.getCreatedImage(id);
    }

}
