package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Device;

import java.util.UUID;

public interface DeviceRepository extends BaseRepository<Device>{

    Page<Device> findByAccountId(UUID accountId, Pageable pageable);

    Page<Device> findByGroupId(UUID groupId, Pageable pageable);

    Page<Device> findByType(String type, Pageable pageable);

    Page<Device> findByStatus(String status, Pageable pageable);

    Device getDeviceBySerial(UUID serialId);
    boolean existsBySerial(UUID serial);

}
