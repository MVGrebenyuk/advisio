package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Details;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.UUID;

public interface DetailsRepository extends BaseRepository<Details> {

    Page<Details> findByAccountId(UUID accountId, Pageable pageable);

    Page<Details> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(
            String namePart, String surnamePart, Pageable pageable);

    boolean existsByAccountId(UUID accountId);

}
