package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Device;
import ru.advisio.core.repository.base.BaseImagedRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends BaseImagedRepository<Device> {

    Page<Device> findByCompanyId(UUID accountId, Pageable pageable);

    Page<Device> findByGroupId(UUID groupId, Pageable pageable);

    Optional<Device> getDeviceBySerial(UUID serialId);
    boolean existsBySerial(UUID serial);

}
