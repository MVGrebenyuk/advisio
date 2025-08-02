package ru.advisio.core.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.advisio.core.entity.Image;

import java.util.List;
import java.util.UUID;

@Repository
public class ImageRepositoryCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Image> getImagesByTypeId(String table, String field, UUID uuid) {
        String sql = String.format(
                "SELECT * FROM image WHERE id IN (SELECT image_id FROM %s WHERE %s = :uuid)",
                table, field
        );

        return entityManager.createNativeQuery(sql, Image.class)
                .setParameter("uuid", uuid)
                .getResultList();
    }
}