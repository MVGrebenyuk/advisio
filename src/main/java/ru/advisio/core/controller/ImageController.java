package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.services.AwsService;
import ru.advisio.core.services.DeviceService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "REQUEST API", description = "Контроллер для обработки запросов на трип")
public class ImageController {

    private final AwsService awsService;
    private final DeviceService deviceService;

    @PostMapping("/upload")
    public String upload(@RequestBody MultipartFile file, @RequestParam(name = "serial", required = true) String serial){
        return awsService.uploadImage(file, serial);
    }

    @GetMapping("/upload")
    public String getBySerial(@RequestParam(name = "serial") String serial){
        return deviceService.getBySerial(serial);
    }

}
