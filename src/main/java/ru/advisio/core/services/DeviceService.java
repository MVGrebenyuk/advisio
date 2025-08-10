package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.device.DeviceRegisterResponseDto;
import ru.advisio.core.dto.device.LinkDeviceDto;
import ru.advisio.core.dto.device.UnlinkDeviceDto;
import ru.advisio.core.entity.Device;
import ru.advisio.core.enums.DeviceType;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.enums.Status;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.repository.DeviceRepository;
import ru.advisio.core.repository.SalePointRepository;
import ru.advisio.core.utils.CollectionObjectMapper;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {
    private final SalePointRepository salePointRepository;
    private final CompanyRepository companyRepository;

    private final DeviceRepository deviceRepository;
    private final CollectionObjectMapper collectionObjectMapper;

    public DeviceRegisterResponseDto registration(String uuid){
        var result = Optional.of(deviceRepository.save(Device.builder()
                        .id(UUID.randomUUID())
                        .type(DeviceType.VISIO_1)
                        .status(Status.ACTIVE)
                        .serial(UUID.fromString(uuid))
                .build())).orElseThrow(() -> new RuntimeException("Unable to register device with id " + uuid));
        log.info("Устройство с id {} успешно зарегистрировано", uuid);
        return (DeviceRegisterResponseDto) collectionObjectMapper.convertValue(result, DeviceRegisterResponseDto.class);
    }

    public boolean isRegister(String id){
        log.info("Запрос существования {}", id);
        return deviceRepository.existsById(UUID.fromString(id));
    }

    @Transactional
    public void linkDevice(LinkDeviceDto linkDeviceDto) {
        var device = deviceRepository.getDeviceBySerial(UUID.fromString(linkDeviceDto.getSerial()))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, linkDeviceDto.getSerial()));
        var salePoint = salePointRepository.findById(UUID.fromString(linkDeviceDto.getSpId()))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, linkDeviceDto.getSpId()));
        device.setSalePoint(salePoint);
        device.setCompany(salePoint.getCompany());
    }

    @Transactional
    public void linkDevice(String cname, String serial) {
        var device = deviceRepository.getDeviceBySerial(UUID.fromString(serial))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, serial));
        device.setCompany(companyRepository.findByCname(cname).orElseThrow(() -> new AdvisioEntityNotFound(EnType.COMPANY, cname)));
    }

    @Transactional
    public void removeFromSalesPoint(UnlinkDeviceDto unlinkDeviceDto){
        var device = deviceRepository.getDeviceBySerial(UUID.fromString(unlinkDeviceDto.getSerial()))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, unlinkDeviceDto.getSerial()));
        device.setSalePoint(null);
    }
}
