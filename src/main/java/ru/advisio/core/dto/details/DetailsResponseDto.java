package ru.advisio.core.dto.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.advisio.core.entity.Company;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailsResponseDto {

    @NotNull
    private UUID id;

    @JsonIgnore
    private Company company;

    @Size(max = 100)
    private String officialName;

}
