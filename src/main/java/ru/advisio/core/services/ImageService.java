package ru.advisio.core.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.advisio.core.entity.base.BaseImagedEntity;
import ru.advisio.core.entity.Image;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.AccountRepository;
import ru.advisio.core.repository.base.BaseImagedRepository;
import ru.advisio.core.repository.DeviceRepository;
import ru.advisio.core.repository.GroupRepository;
import ru.advisio.core.repository.ImageRepository;
import ru.advisio.core.repository.SalePointRepository;
import ru.advisio.core.repository.custom.ImageRepositoryCustomImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository repository;
    private final ImageRepositoryCustomImpl imageRepositoryCustom;
    private final DeviceRepository deviceRepository;
    private final AccountRepository accountRepository;
    private final SalePointRepository salePointRepository;
    private final GroupRepository groupRepository;
    private Map<EnType, BaseImagedRepository<? extends BaseImagedEntity>> dinamicRepo;
    @Value("${image.default}")
    private String defaultImage;

    @PostConstruct
    public void init(){
     dinamicRepo = Map.of(
                EnType.DEVICE, deviceRepository,
                EnType.SP, salePointRepository,
                EnType.ACCOUNT, accountRepository,
                EnType.GROUP, groupRepository);
    }

    public Image saveImage(String image, UUID id, EnType enType){
        log.info("Сохраняем изображение {}", image);
        var result = repository.save(Image.builder()
                        .id(UUID.randomUUID())
                        .image(image)
                .build());

        if(id != null && enType != null){
            log.info("Привязка изображения к {} с id {}", enType.name(), id);
            link(result, id, enType);
        }

        return result;
    }
    public void link(Image image, UUID id, EnType enType) {
        dinamicRepo.get(enType).findById(id)
                .orElseThrow(() -> new AdvisioEntityNotFound(enType, id))
                .getImages().add(image);

    }

    public List<Image> getImagesByType(String uuid, EnType type) {
        log.info("Попытка получения данных для отображения для {} с id {}", type.name(), uuid);
        var result = imageRepositoryCustom.getImagesByTypeId(type.getM2mTable(), type.getField(), UUID.fromString(uuid));

        if (CollectionUtils.isEmpty(result)){
            log.info("Изображение для отображения для {} с id {} не найдено. Применяем дефолтное", type.name(), uuid);
            return List.of(Image.builder()
                    .id(UUID.randomUUID())
                    .image(defaultImage)
                    .build());
        }

        return result;
    }
}
