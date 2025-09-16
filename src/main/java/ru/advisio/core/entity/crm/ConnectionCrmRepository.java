package ru.advisio.core.entity.crm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConnectionCrmRepository extends JpaRepository<ConnectionCrm, UUID> {
}