package ru.advisio.core.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.dto.image.ImageResponseDto;
import ru.advisio.core.entity.Image;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.DeviceRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${aws.path}")
    private String path;

    private final S3Client client;
    private final ImageService imageService;

    @Transactional
    public ImageResponseDto uploadImage(MultipartFile file) {
        var response = uploadImageToS3(file);

        if(response == null) {
            log.error("Error save image");
            throw new RuntimeException();
        }

        return imageService.saveImage(response, null, null);
    }

    @Transactional
    public ImageResponseDto uploadImage(MultipartFile file, String id, EnType type) {
        var response = uploadImageToS3(file);

        if(response == null) {
            log.error("Error save image");
            throw new RuntimeException();
        }

        return imageService.saveImage(response, UUID.fromString(id), type);
    }

    public String uploadImageToS3(MultipartFile file) {
        String partName = UUID.randomUUID().toString();
        try {
            client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .key(partName + file.getOriginalFilename())
                    .build(), RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return path + partName + file.getOriginalFilename();
    }

}
