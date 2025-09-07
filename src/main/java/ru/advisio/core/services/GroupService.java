package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.group.CreateGroupDto;
import ru.advisio.core.dto.group.GroupDto;
import ru.advisio.core.dto.group.ShortGroupDto;
import ru.advisio.core.dto.group.UpdateGroupDto;
import ru.advisio.core.entity.Group;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.enums.Status;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.repository.DeviceRepository;
import ru.advisio.core.repository.GroupRepository;
import ru.advisio.core.repository.SalePointRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupService {
    private final SalePointRepository salePointRepository;
    private final CompanyRepository companyRepository;
    private final DeviceRepository deviceRepository;

    private final GroupRepository repository;
    
    public List<ShortGroupDto> getGroupsByCname(String cname){
        return repository.findAllByCompany_Cname(cname)
                .stream()
                .map(entity -> ShortGroupDto.builder()
                        .id(String.valueOf(entity.getId()))
                        .name(entity.getName())
                        .count(entity.getDevices().stream().count())
                        .isActive(entity.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }
    
    public GroupDto getGroupById(String uuid){
        var entity =  repository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.GROUP, uuid));
        
        return mapToGroup(entity);
    }
    
    public GroupDto.GroupDevices mapRoSpDevices(Group group){
        Map<String, List<GroupDto.ShortDeviceDto>> result = new HashMap<>();

        return mapToGroupDevices(group, result);
    }

    public GroupDto addDeviceToGroup(String groupId, String deviceId) {
        var group = repository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.GROUP, groupId));

        var device = deviceRepository.findById(UUID.fromString(deviceId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, deviceId));

        group.getDevices().add(device);
        device.setGroup(group);

        return mapToGroup(group);
    }

    public GroupDto removeDeviceFromGroup(String groupId, String deviceId) {
        var group = repository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.GROUP, groupId));
        var device = deviceRepository.findById(UUID.fromString(deviceId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, deviceId));

        group.getDevices().remove(device);
        device.setGroup(null);

        return mapToGroup(group);
    }

    public GroupDto addAllDeviceFromGroupBySp(String groupId, String spId) {
        var group = repository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.GROUP, groupId));

        var salePoint = salePointRepository.findById(UUID.fromString(spId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, spId));

        group.getDevices().addAll(salePoint.getDevices());

        return mapToGroup(group);
    }

    public GroupDto removeAllDeviceFromGroupBySp(String groupId, String spId) {
        var group = repository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.GROUP, groupId));

        var salePoint = salePointRepository.findById(UUID.fromString(spId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, spId));

        group.getDevices().removeAll(salePoint.getDevices());

        return mapToGroup(group);
    }

    public GroupDto createGroup(String cname, CreateGroupDto createGroupDto) {
        var group = repository.save(Group.builder()
                        .id(UUID.randomUUID())
                        .name(createGroupDto.getName())
                        .devices(new ArrayList<>())
                        .images(new ArrayList<>())
                        .company(companyRepository.findByCname(cname)
                                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.COMPANY, cname)))
                        .isActive(false)
                        .build());

        return mapToGroup(group);
    }

    public GroupDto updateGroup(String cname, String groupId, UpdateGroupDto updateGroupDto) {
        log.info("Активация/обновление группы {} {}", cname, groupId);
        var group = repository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.GROUP, groupId));

        group.setName(updateGroupDto.getName() == null ? group.getName() : updateGroupDto.getName());
        group.setIsActive(updateGroupDto.isActive());

        log.info("Группа {} активирована/обновлена", group.getName());
        return mapToGroup(group);
    }

    private GroupDto mapToGroup(Group group) {
        return GroupDto.builder()
                .id(group.getId().toString())
                .name(group.getName())
                .count(group.getDevices().stream().count())
                .isActive(group.getIsActive())
                .groupDevices(mapRoSpDevices(group))
                .build();
    }

    private static GroupDto.GroupDevices mapToGroupDevices(Group group, Map<String, List<GroupDto.ShortDeviceDto>> result) {
        group.getDevices()
                .forEach(device -> {

                    if(device.getSalePoint() == null){
                        throw new AdvisioEntityNotFound(EnType.SP,"Неизвестная точка при маппинге группы");
                    }

                    if(result.containsKey(device.getSalePoint().getName())){
                        result.get(device.getSalePoint().getName())
                                .add(GroupDto.ShortDeviceDto.builder()
                                .id(device.getId().toString())
                                .isActive(device.getStatus() == Status.ACTIVE)
                                .name(device.getSerial().toString())
                                .build());
                    } else {
                        result.put(device.getSalePoint().getName(), new ArrayList<>(Arrays.asList(GroupDto.ShortDeviceDto.builder()
                                .id(device.getId().toString())
                                .isActive(device.getStatus() == Status.ACTIVE)
                                .name(device.getSerial().toString())
                                .build())));
                    }
                });
        return new GroupDto.GroupDevices(result);
    }
}
