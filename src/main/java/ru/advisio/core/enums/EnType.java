package ru.advisio.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Перечисление типов сущностей", example = "COMPANY, SP, GROUP, DEVICE")
@Getter
@AllArgsConstructor
public enum EnType {

    COMPANY("company_images","company_id"),
    SP("salepoint_images","sp_id"),
    GROUP("dev_group_images","group_id"),
    DEVICE("device_images","device_id"),
    DETAILS(null, null),
    TAG(null, null),
    TEMPLATE(null, null),
    FILE(null, null),
    IMAGE(null, null),
    CRM(null, null);

    private final String m2mTable;
    private final String field;

}
