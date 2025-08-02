package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.advisio.core.entity.Device;
import ru.advisio.core.entity.Image;
import ru.advisio.core.enums.DeviceType;
import ru.advisio.core.enums.Status;
import ru.advisio.core.repository.DeviceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    public UUID registration(String uuid){
        var result = Optional.of(deviceRepository.save(Device.builder()
                        .id(UUID.randomUUID())
                        .type(DeviceType.VISIO_1)
                        .status(Status.ACTIVE)
                        .serial(UUID.fromString(uuid))
                .build())).orElseThrow(() -> new RuntimeException("Unable to register device with id " + uuid));
        log.info("Устройство с id {} успешно зарегистрировано", uuid);
        return result.getId();
    }

    public boolean isRegister(String id){
        log.info("Запрос существования {}", id);
        return deviceRepository.existsById(UUID.fromString(id));
    }
}
