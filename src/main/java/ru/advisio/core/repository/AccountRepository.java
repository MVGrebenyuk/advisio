package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Account;

public interface AccountRepository extends BaseRepository<Account> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Page<Account> findByEmailContainingIgnoreCase(String emailPart, Pageable pageable);

}
