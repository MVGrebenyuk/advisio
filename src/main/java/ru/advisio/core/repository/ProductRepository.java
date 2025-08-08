package ru.advisio.core.repository;

import ru.advisio.core.entity.products.Product;
import ru.advisio.core.repository.base.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends BaseRepository<Product> {

    List<Product> findAllByCrmSource_Id(UUID crmId);

}
