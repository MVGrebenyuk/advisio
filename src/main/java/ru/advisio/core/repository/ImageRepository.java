package ru.advisio.core.repository;

import org.springframework.data.jpa.repository.Query;
import ru.advisio.core.entity.Image;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.List;

public interface ImageRepository extends BaseRepository<Image> {

    @Query(value = "select * from image where id in(select image_id from :table where :field = :uuid)", nativeQuery = true)
    List<Image> getImagesByTypeId(String table, String field, String uuid);

}
