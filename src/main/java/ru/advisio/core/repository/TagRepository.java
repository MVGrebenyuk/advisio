package ru.advisio.core.repository;

import ru.advisio.core.entity.Tag;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends BaseRepository<Tag> {

    int deleteTagByName(String name);

    int deleteTagById(UUID id);

    List<Tag> getTagsByCompany_Cname(String cname);

}
