package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.entity.Image;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.services.AwsService;
import ru.advisio.core.services.DeviceService;
import ru.advisio.core.services.ImageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "REQUEST API", description = "Контроллер для обработки запросов на трип")
@RequestMapping("/image")
public class ImageController {

    private final AwsService awsService;
    private final ImageService imageService;

    @PostMapping("/upload")
    @Operation(description = "Загрузка изображения с привязкой в зависимости от типа")
    public Image uploadAndLink(@RequestBody MultipartFile file, @RequestParam(name = "serial", required = false) String serial, @RequestParam(required = false, name = "type")EnType type){
        return awsService.uploadImage(file, serial, type);
    }

    @GetMapping("/{id}/download")
    @Operation(description = "Получение изображений по id сущности, в зависимости от типа (ACCOUNT, SP, DEVICE, GROUP)")
    public List<Image> getImages(@PathVariable(name = "id") String uuid, @RequestParam(required = true) EnType type){
        return imageService.getImagesByType(uuid, type);
    }
}
