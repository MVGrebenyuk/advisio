package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        log.info("Устройство с id {} успешно зарегистрировано", uuid);
        return true;
    }

    public boolean isRegister(String serialId){
        log.info("Запрос существования {}", serialId);
        return deviceRepository.existsBySerial(UUID.fromString(serialId));
    }

    public String getBySerial(String serial) {
        log.info("Получение изображения {}", serial);
        var image =  deviceRepository.getDeviceBySerial(UUID.fromString(serial))
                .getImageUrl();

        if(StringUtils.isEmpty(image)){
            log.info("Стандартного изображения для {} нет. Загружаем дефолтное", serial);
            return "https://advisio.hb.ru-msk.vkcloud-storage.ru/f5dd7a07-5bd9-4777-af66-fc10ddd23ef4%D1%8F2.jpg";
        }

        return image;
    }
}
