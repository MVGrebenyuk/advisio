package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Account;
import ru.advisio.core.repository.base.BaseImagedRepository;

public interface AccountRepository extends BaseImagedRepository<Account> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Page<Account> findByEmailContainingIgnoreCase(String emailPart, Pageable pageable);

}
