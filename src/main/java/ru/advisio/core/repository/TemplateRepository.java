package ru.advisio.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.advisio.core.entity.Template;
import ru.advisio.core.repository.base.BaseRepository;

public interface TemplateRepository extends BaseRepository<Template> {

    Page<Template> findAllByCompany_Cname(String cname, Pageable pageable);

}
