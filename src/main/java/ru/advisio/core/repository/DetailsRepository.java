package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Details;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DetailsRepository extends BaseRepository<Details> {

    Optional<Details> findByAccountId(UUID accountId);

    boolean existsByAccountId(UUID accountId);

}
