package ru.advisio.core.dto.details;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность для обогащения или изменения details")
public class DetailsUpdateDto {

    @Id
    @NotNull
    private UUID id;

    @NotNull
    private String accountId;

    @Size(max = 100)
    private String officialName;

}
