package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.SalePoint;
import ru.advisio.core.repository.base.BaseImagedRepository;

import java.util.List;

public interface SalePointRepository extends BaseImagedRepository<SalePoint> {

    Page<SalePoint> findByNameContainingIgnoreCase(String namePart, Pageable pageable);

    List<SalePoint> findAllByCompany_CnameIgnoreCase(String cname);

}
