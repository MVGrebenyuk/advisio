package ru.advisio.core.dto.editor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ImageDataToSaveDto {

    @NotEmpty
    private MultipartFile file;

    @Nullable
    private String templateId;

    @NotEmpty
    private String htmlDataToRegenerate;

    private String crmId;

    @Schema(description = "Заполняется, если выполняется редактирование уже существующего сreated изображения")
    private String existedImageId;

}
