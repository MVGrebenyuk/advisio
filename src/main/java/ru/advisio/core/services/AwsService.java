package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.dto.editor.ImageDataToSaveDto;
import ru.advisio.core.dto.image.ImageResponseDto;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
    private final CompanyService companyService;

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
    public ImageResponseDto uploadImageFromEditor(ImageDataToSaveDto data, EnType type, String cname) {
        if(data.getFile() == null){
            throw new AdvisioEntityNotFound(EnType.FILE, data.getTemplateId());
        }

        var response = uploadImageToS3(data.getFile());

        if(response == null) {
            log.error("Error save image");
            throw new RuntimeException();
        }



        return imageService.saveImage(response, companyService.getSafeCompanyByCname(cname).getId(), type, data);
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
