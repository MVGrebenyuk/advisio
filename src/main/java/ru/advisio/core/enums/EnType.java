package ru.advisio.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Перечисление типов сущностей", example = "ACCOUNT, SP, GROUP, DEVICE")
@Getter
@AllArgsConstructor
public enum EnType {

    ACCOUNT("account_images","account_id"),
    SP("salepoint_images","sp_id"),
    GROUP("dev_group_images","group_id"),
    DEVICE("device_images","device_id"),
    DETAILS(null, null);

    private final String m2mTable;
    private final String field;

}
