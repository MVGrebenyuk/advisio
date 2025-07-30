package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.SalePoint;

import java.util.UUID;

public interface SalePointRepository extends BaseRepository<SalePoint> {

    Page<SalePoint> findByAccountId(UUID accountId, Pageable pageable);

    Page<SalePoint> findByNameContainingIgnoreCase(String namePart, Pageable pageable);

    long countByAccountId(UUID accountId);

}
