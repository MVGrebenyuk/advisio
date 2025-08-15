package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.crm.AdditionalParams;
import ru.advisio.core.dto.crm.CrmData;
import ru.advisio.core.dto.products.ProductsCreateRequest;
import ru.advisio.core.entity.products.Product;
import ru.advisio.core.entity.products.ProductAttribute;
import ru.advisio.core.repository.ProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductsService {

    private final ProductRepository productRepository;

    @Transactional
    public List<CrmData> getProducts(String cname, String crmId) {
        List<Product> products = productRepository.findAllByCrmSource_Id(UUID.fromString(crmId));
        products.stream()
                .map(product -> CrmData.builder()
                        .id(product.getId().toString())
                        .techName(product.getTechName())
                        .saleName(product.getSaleName())
                        .value(product.getValue())
                        .additional(CollectionUtils.isEmpty(product.getAttributes()) ?
                                CollectionUtils.EMPTY_COLLECTION : generateAdditionalParams(product))
                        .build());
    }

    private List<AdditionalParams> generateAdditionalParams(Product product) {
        return product.getAttributes()
                .stream()
                .map(attr -> AdditionalParams.builder()
                        .id(attr.getId().toString())
                        .techName(attr.getTechName())
                        .techParams(CollectionUtils.isEmpty(product.getAttributes()) ?
                                (List<AdditionalParams>) CollectionUtils.EMPTY_COLLECTION : generateTechParams(attr))
                        .name(attr.getTechName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<AdditionalParams> generateTechParams(ProductAttribute attr) {
        return attr.getTe
                .stream()
                .map(attr -> AdditionalParams.builder()
                        .id(attr.getId().toString())
                        .techName(attr.getTechName())
                        .techParams(CollectionUtils.isEmpty(product.getAttributes()) ?
                                (List<AdditionalParams>) CollectionUtils.EMPTY_COLLECTION : generateTechParams(attr))
                        .name(attr.getTechName())
                        .build())
                .collect(Collectors.toList());
    }

    public ProductsCreateRequest createProducts(String cname, String crmId,ProductsCreateRequest request) {
    }

    public ProductsCreateRequest updateProducts(String cname, String crmId, ProductsCreateRequest request) {
    }
}
