package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.advisio.core.entity.Group;
import ru.advisio.core.repository.base.BaseImagedRepository;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.UUID;

public interface GroupRepository extends BaseImagedRepository<Group> {

    Page<Group> findAll(Specification<Group> spec, Pageable pageable);

    Page<Group> findByNameContainingIgnoreCase(String namePart, Pageable pageable);

}
