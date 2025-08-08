package ru.advisio.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "Тип CRM")
public enum CrmType {

    LOCAL("Локальный источник данных (без CRM)"),
    ONE_C_8("1C, версия 8+"),
    ONE_C_7("1C, версия 7");

    private final String name;

}
