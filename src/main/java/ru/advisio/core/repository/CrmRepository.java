package ru.advisio.core.repository;

import ru.advisio.core.entity.crm.Crm;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface CrmRepository extends BaseRepository<Crm> {

    List<Crm> findAllByCompanyId(UUID companyId);

}
