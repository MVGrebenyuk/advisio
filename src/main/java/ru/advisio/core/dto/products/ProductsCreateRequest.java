package ru.advisio.core.dto.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.dto.crm.CrmProductData;
import ru.advisio.core.enums.CrmType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsCreateRequest {

    private String crmId;

    private CrmType crmType;

    private List<CrmProductData> crmProductDataList;

}
