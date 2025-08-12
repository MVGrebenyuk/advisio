package ru.advisio.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.advisio.core.aop.CompanyManager;
import ru.advisio.core.aop.CompanyObserver;
import ru.advisio.core.dto.group.CreateGroupDto;
import ru.advisio.core.dto.group.GroupDto;
import ru.advisio.core.dto.group.ShortGroupDto;
import ru.advisio.core.dto.group.UpdateGroupDto;
import ru.advisio.core.services.GroupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(name = "GROUP API", description = "Контроллер для работы с группами")
public class GroupController {

    private final GroupService service;

    @CompanyManager
    @PostMapping("/{cname}/create")
    @Operation(description = "Создать новую группу")
    public GroupDto createGroup(@PathVariable String cname, @RequestBody CreateGroupDto createGroupDto){
        return service.createGroup(cname, createGroupDto);
    }

    @CompanyManager
    @PutMapping("/{cname}/{groupId}/update")
    @Operation(description = "Обновить группу (в т.ч. активировать)")
    public GroupDto updateGroup(@PathVariable String cname, @PathVariable String groupId, @RequestBody UpdateGroupDto updateGroupDto){
        return service.updateGroup(cname, groupId, updateGroupDto);
    }

    @CompanyObserver
    @GetMapping("/{cname}/all")
    @Operation(description = "Получить список групп")
    public List<ShortGroupDto> getGroups(@PathVariable String cname){
        return service.getGroupsByCname(cname);
    }

    @CompanyManager
    @GetMapping("/{cname}/{groupId}/get")
    @Operation(description = "Получить конкретную группу")
    public GroupDto getGroupById(@PathVariable String cname, @PathVariable String groupId){
        return service.getGroupById(groupId);
    }

    @CompanyManager
    @PutMapping("/{cname}/{groupId}/device/{deviceId}/add")
    @Operation(description = "Добавить девайс в группу")
    public GroupDto addDeviceToGroup(@PathVariable String cname, @PathVariable String groupId, @PathVariable String deviceId){
        return service.addDeviceToGroup(groupId, deviceId);
    }

    @CompanyManager
    @PutMapping("/{cname}/{groupId}/device/{deviceId}/remove")
    @Operation(description = "Удалить девайс из группы")
    public GroupDto removeDeviceFromGroup(@PathVariable String cname, @PathVariable String groupId, @PathVariable String deviceId){
        return service.removeDeviceFromGroup(groupId, deviceId);
    }

    @CompanyManager
    @PutMapping("/{cname}/{groupId}/salepoint/{spId}/add")
    @Operation(description = "Удалить девайс из группы")
    public GroupDto addAllDevicesFromGroupBySelePoint(@PathVariable String cname, @PathVariable String groupId, @PathVariable String spId){
        return service.addAllDeviceFromGroupBySp(groupId, spId);
    }

    @CompanyManager
    @PutMapping("/{cname}/{groupId}/salepoint/{spId}/remove")
    @Operation(description = "Удалить девайс из группы")
    public GroupDto removeAllDevicesFromGroupBySelePoint(@PathVariable String cname, @PathVariable String groupId, @PathVariable String spId){
        return service.removeAllDeviceFromGroupBySp(groupId, spId);
    }

}
