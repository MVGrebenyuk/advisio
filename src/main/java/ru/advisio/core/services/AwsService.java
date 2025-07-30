package ru.advisio.core.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.repository.DeviceRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {

    @Value("${aws.bucket}")
    private String bucket;
    private String path = "https://advisio.hb.bizmrg.com/";

    private final S3Client client;
    private final DeviceRepository deviceRepository;

    @Transactional
    public String uploadImage(MultipartFile file, String deviceId) {

        if(!deviceRepository.existsBySerial(UUID.fromString(deviceId))){
            log.error("Device with id {} not registered", deviceId);
            throw new EntityNotFoundException();
        }

        var response = uploadImage(file);
        if(response == null) {
            log.error("Error save image for device: {}", deviceId);
            throw new RuntimeException();
        }

        deviceRepository.getDeviceBySerial(UUID.fromString(deviceId))
                .setImageUrl(response);
        log.info("Succesfull saved for device: {}", deviceId);

        return response;
    }
    public String uploadImage(MultipartFile file) {
        String partName = UUID.randomUUID().toString();
        try {
            PutObjectResponse putObjectResponse = client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .acl("public-read")
                    .key(partName + file.getOriginalFilename())
                    .build(), RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return path + partName + file.getOriginalFilename();
    }

}
