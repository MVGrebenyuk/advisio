package ru.advisio.core.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "Описание типа компании (ИП, ООО, АО, ОАО, ПАО)", example = "PERSONAL, LLC, JSC, OJSC, PJSC")
public enum CompanyType {

    PERSONAL("ИП"),
    LLC("ООО"),
    JSC("АО"),
    OJSC("ОАО"),
    PJSC("ПАО");

    private String rusName;
}
