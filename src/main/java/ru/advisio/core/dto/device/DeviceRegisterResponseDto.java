package ru.advisio.core.dto.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRegisterResponseDto {

    private UUID id;

    private UUID serial;

}
