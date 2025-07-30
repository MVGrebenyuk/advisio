package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.advisio.core.entity.Device;
import ru.advisio.core.repository.DeviceRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    public boolean registration(String uuid){
        Optional.of(deviceRepository.save(Device.builder()
                        .id(UUID.randomUUID())
                        .type("VISIO_V1")
                        .status("ACTIVE")
                        .serial(UUID.fromString(uuid))
                .build())).orElseThrow(() -> new RuntimeException("Unable to register device with id " + uuid));

        return true;
    }

    public boolean isRegister(String serialId){
        return deviceRepository.existsBySerial(UUID.fromString(serialId));
    }

    public String getBySerial(String serial) {
        return deviceRepository.getDeviceBySerial(UUID.fromString(serial))
                .getImageUrl();
    }
}
