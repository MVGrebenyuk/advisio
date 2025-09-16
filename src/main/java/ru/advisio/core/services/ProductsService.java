package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.crm.AdditionalParams;
import ru.advisio.core.dto.crm.CrmProductData;
import ru.advisio.core.dto.products.ProductsCreateRequest;
import ru.advisio.core.entity.products.Product;
import ru.advisio.core.entity.products.ProductAttribute;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CrmRepository;
import ru.advisio.core.repository.ProductAttributeRepository;
import ru.advisio.core.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductsService {
    private final ProductAttributeRepository productAttributeRepository;
    private final CrmRepository crmRepository;

    private final ProductRepository productRepository;

    public List<CrmProductData> getProducts(String cname, String crmId) {
        List<Product> products = productRepository.findAllByCrmSource_Id(UUID.fromString(crmId));
        return productsToCrmData(products);
    }

    private List<CrmProductData> productsToCrmData(List<Product> products) {
        return products.stream()
                .map(product -> CrmProductData.builder()
                        .id(product.getId().toString())
                        .techName(product.getTechName())
                        .saleName(product.getSaleName())
                        .value(product.getValue())
                        .additional(CollectionUtils.isEmpty(product.getAttributes()) ?
                                (List<AdditionalParams>) CollectionUtils.EMPTY_COLLECTION : generateAdditionalParams(product))
                        .build())
                .collect(Collectors.toList());
    }

    private List<AdditionalParams> generateAdditionalParams(Product product) {
        return product.getAttributes()
                .stream()
                .map(attr -> AdditionalParams.builder()
                        .id(attr.getId().toString())
                        .techName(attr.getTechName())
                        .name(attr.getTechName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CrmProductData> createProducts(String cname, String crmId, ProductsCreateRequest request) {
        var crm = crmRepository.findById(UUID.fromString(crmId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.CRM, crmId));
        var products = request.getCrmProductDataList()
                .stream()
                .map(crmProductData -> {
                   var product =  Product.builder()
                            .id(UUID.randomUUID())
                            .techName(crmProductData.getTechName())
                            .saleName(crmProductData.getSaleName())
                            .crmSource(crm)
                            .value(crmProductData.getValue())
                            .build();

                   if(!CollectionUtils.isEmpty(crmProductData.getAdditional())) {
                       product.setAttributes(crmProductData.getAdditional()
                               .stream().map(attr -> ProductAttribute.builder()
                                       .id(UUID.randomUUID())
                                       .product(product)
                                       .techId(attr.getTechName())
                                       .value(attr.getValue())
                                       .saleName(attr.getName())
                                       .techName(attr.getTechName())
                                       .build())
                               .collect(Collectors.toList()));
                   }

                   return product;
                }).collect(Collectors.toList());

        return productsToCrmData(products);
    }

    public List<CrmProductData> updateProducts(String cname, String crmId, ProductsCreateRequest request) {
        var ids = request.getCrmProductDataList().stream().map(CrmProductData::getId).toList();

        var products = productRepository.findAllByCrmSource_Id(UUID.fromString(crmId));

                products.stream().filter(product -> ids.contains(product.getId().toString()))
                .forEach(product -> {
                    var data = request.getCrmProductDataList()
                            .stream()
                            .filter(updateData -> UUID.fromString(updateData.getId()).equals(product.getId()))
                            .findFirst().orElseThrow();

                    product.setTechName(data.getTechName());
                    product.setSaleName(data.getSaleName());
                    product.setValue(data.getValue());

                    if(CollectionUtils.isEmpty(data.getAdditional())){
                        product.setAttributes(new ArrayList<>());
                    } else {
                        var attrIds = data.getAdditional().stream().map(AdditionalParams::getId).toList();
                        product.getAttributes().stream()
                                .filter(attr -> attrIds.contains(attr.getId().toString()))
                                .forEach(attr -> {
                                    var attrData = data.getAdditional()
                                            .stream()
                                            .filter(additional -> additional.getId().equals(attr.getId().toString()))
                                            .findFirst()
                                            .orElseThrow();

                                    attr.setValue(attrData.getValue());
                                    attr.setSaleName(attrData.getName());
                                    attr.setTechName(attrData.getTechName());
                                });
                    }

                    //ToDo добавить обратную логику добавления несуществующего параметра
                });

        return productsToCrmData(products);
    }

    public boolean deleteProduct(String crmdId, UUID productId) {
        productRepository.deleteById(productId);
        return true;
    }

    public boolean deleteAdditionalParam(String crmdId, UUID additionalParamId) {
        productAttributeRepository.deleteById(additionalParamId);
        return true;
    }
}
