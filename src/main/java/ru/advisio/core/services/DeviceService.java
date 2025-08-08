package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.device.DeviceDto;
import ru.advisio.core.dto.device.DeviceRegisterResponseDto;
import ru.advisio.core.dto.device.LinkDeviceDto;
import ru.advisio.core.dto.device.UnlinkDeviceDto;
import ru.advisio.core.dto.device.enums.Source;
import ru.advisio.core.entity.Device;
import ru.advisio.core.entity.Tag;
import ru.advisio.core.enums.DeviceType;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.enums.Status;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.repository.DeviceRepository;
import ru.advisio.core.repository.SalePointRepository;
import ru.advisio.core.repository.TagRepository;
import ru.advisio.core.utils.CollectionObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {
    private final TagRepository tagRepository;
    private final SalePointRepository salePointRepository;
    private final CompanyRepository companyRepository;

    private final DeviceRepository deviceRepository;
    private final CollectionObjectMapper collectionObjectMapper;

    public DeviceRegisterResponseDto registration(String uuid){
        var result = Optional.of(deviceRepository.save(Device.builder()
                        .id(UUID.fromString(uuid))
                        .type(DeviceType.VISIO_1)
                        .status(Status.DISABLE)
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

    @Transactional
    public List<DeviceDto> getAllDevices(String cname) {
       return deviceRepository.findByCompany_Cname(cname)
                .stream()
                .map(entity -> DeviceDto.builder()
                        .id(entity.getId().toString())
                        .tag(entity.getTag() == null ? null :
                                Tag.builder()
                                        .id(entity.getTag().getId())
                                        .name(entity.getTag().getName())
                                .build())
                        .source(entity.getGroup() == null || !entity.getGroup().getIsActive() ? Source.SP : Source.GROUP)
                        .isActive(entity.getStatus() == Status.ACTIVE)
                        .salePoint(entity.getSalePoint() == null ? null : DeviceDto.SalePointDto.builder()
                                .id(String.valueOf(entity.getSalePoint().getId()))
                                .name(entity.getSalePoint().getName())
                                .build())
                        .serial(String.valueOf(entity.getSerial()))
                        .build())
                .collect(Collectors.toList());

    }

    @Transactional
    public List<DeviceDto> getDevicesBySalePoint(String spId) {
        var res =  deviceRepository.findBySalePoint_Id(UUID.fromString(spId))
                .stream()
                .map(mapEntityToDeviceDto())
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(res) ? new ArrayList<>() : res;

    }

    @Transactional
    public List<DeviceDto> getDevicesByGroup(String groupId) {
        var res =  deviceRepository.findBySalePoint_Id(UUID.fromString(groupId))
                .stream()
                .map(mapEntityToDeviceDto())
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(res) ? new ArrayList<>() : res;
    }

    @Transactional
    public List<DeviceDto> getDevicesByTagId(String tagId) {
        var res = deviceRepository.findBySalePoint_Id(UUID.fromString(tagId))
                .stream()
                .map(mapEntityToDeviceDto())
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(res) ? new ArrayList<>() : res;
    }

    private static Function<Device, DeviceDto> mapEntityToDeviceDto() {
        return entity -> DeviceDto.builder()
                .id(entity.getId().toString())
                .tag(entity.getTag() == null ? null :
                        Tag.builder()
                                .id(entity.getTag().getId())
                                .name(entity.getTag().getName())
                                .build())
                .source(entity.getGroup() == null ? Source.SP : Source.GROUP)
                .isActive(entity.getStatus() == Status.ACTIVE)
                .salePoint(entity.getSalePoint() == null ? null : DeviceDto.SalePointDto.builder()
                        .id(String.valueOf(entity.getSalePoint().getId()))
                        .name(entity.getSalePoint().getName())
                        .build())
                .serial(String.valueOf(entity.getSerial()))
                .build();
    }

    @Transactional
    public boolean setTag(String deviceId, String tagId) {
        var entity = deviceRepository.findById(UUID.fromString(deviceId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, deviceId));
        var tag = tagRepository.findById(UUID.fromString(tagId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, tagId));
        entity.setTag(tag);
        return true;
    }
}
