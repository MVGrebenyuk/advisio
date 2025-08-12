package ru.advisio.core.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDto {

    private String id;

    private String name;

    private Long count;

    private boolean isActive;

    private GroupDevices groupDevices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GroupDevices {
        Map<String, List<ShortDeviceDto>> map;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShortDeviceDto {
        private String id;
        private boolean isActive;
        private String name;
    }

}
