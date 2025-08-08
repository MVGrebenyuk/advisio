package ru.advisio.core.repository;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Company;
import ru.advisio.core.repository.base.BaseImagedRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CompanyRepository extends BaseImagedRepository<Company> {
    Optional<Company> findByCname(String name);

    Set<Company> findCompaniesByCnameIn(Set<String> cnames);

    boolean existsCompanyByCname(String name);

}
