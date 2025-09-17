package ru.advisio.core.services;

import jakarta.annotation.PostConstruct;
import liquibase.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.editor.ImageDataToSaveDto;
import ru.advisio.core.dto.image.ImageResponseDto;
import ru.advisio.core.entity.Group;
import ru.advisio.core.entity.SalePoint;
import ru.advisio.core.entity.base.BaseImagedEntity;
import ru.advisio.core.entity.Image;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.repository.CrmRepository;
import ru.advisio.core.repository.TemplateRepository;
import ru.advisio.core.repository.base.BaseImagedRepository;
import ru.advisio.core.repository.DeviceRepository;
import ru.advisio.core.repository.GroupRepository;
import ru.advisio.core.repository.ImageRepository;
import ru.advisio.core.repository.SalePointRepository;
import ru.advisio.core.repository.custom.ImageRepositoryCustomImpl;
import ru.advisio.core.utils.CollectionObjectMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final TemplateRepository templateRepository;

    private final CollectionObjectMapper<ImageResponseDto, Image> objectMapper;

    private final ImageRepository repository;
    private final ImageRepositoryCustomImpl imageRepositoryCustom;
    private final DeviceRepository deviceRepository;
    private final CompanyRepository companyRepository;
    private final SalePointRepository salePointRepository;
    private final GroupRepository groupRepository;
    private final CompanyService companyService;
    private final CrmRepository crmRepository;
    private Map<EnType, BaseImagedRepository<? extends BaseImagedEntity>> dinamicRepo;
    @Value("${image.default}")
    private String defaultImage;

    @PostConstruct
    public void init(){
     dinamicRepo = Map.of(
                EnType.DEVICE, deviceRepository,
                EnType.SP, salePointRepository,
                EnType.COMPANY, companyRepository,
                EnType.GROUP, groupRepository);
    }

    public ImageResponseDto saveImage(String image, UUID id, EnType enType){
        log.info("Сохраняем изображение {}", image);
        var result = repository.save(Image.builder()
                        .id(UUID.randomUUID())
                        .image(image)
                .build());

        if(id != null && enType != null){
            log.info("Привязка изображения к {} с id {}", enType.name(), id);
            link(result, id, enType);
        }

        return objectMapper.convertValue(result, ImageResponseDto.class);
    }

    @Transactional
    public ImageResponseDto saveImage(String image, UUID id, EnType enType,
                                      ImageDataToSaveDto data){
        log.info("Сохраняем изображение {}", image);
        Image result;

        if(data.getExistedImageId() != null){
            result = repository.findById(UUID.fromString(data.getExistedImageId()))
                    .orElseThrow(() -> new AdvisioEntityNotFound(EnType.IMAGE, data.getExistedImageId()));
            result.setImage(image);
            result.setData(data.getHtmlDataToRegenerate());
            result.setCrmId(crmRepository.findById(UUID.fromString(data.getCrmId()))
                    .orElse(null));
            result.setTemplate(templateRepository.findById(UUID.fromString(data.getTemplateId()))
                    .orElse(null));
        } else {
            result = repository.save(Image.builder()
                    .id(UUID.randomUUID())
                    .image(image)
                    .crmId(crmRepository.findById(UUID.fromString(data.getCrmId()))
                            .orElse(null))
                    .template(templateRepository.findById(UUID.fromString(data.getTemplateId()))
                            .orElse(null))
                    .data(data.getHtmlDataToRegenerate())
                    .build());
        }

        if(id != null && enType != null && data.getExistedImageId() == null){
            log.info("Привязка изображения к {} с id {}", enType.name(), id);
            link(result, id, enType);
        }

        return objectMapper.convertValue(result, ImageResponseDto.class);
    }
    public void link(Image image, UUID id, EnType enType) {
        dinamicRepo.get(enType).findById(id)
                .orElseThrow(() -> new AdvisioEntityNotFound(enType, id))
                .getImages().add(image);

    }

    @Deprecated
    @Transactional
    public List<ImageResponseDto> getImagesByType(String uuid, EnType type) {
        log.info("Попытка получения данных для отображения для {} с id {}", type.name(), uuid);
        var result = imageRepositoryCustom.getImagesByTypeId(type.getM2mTable(), type.getField(), UUID.fromString(uuid));

        if (CollectionUtils.isEmpty(result)){
            log.info("Изображение для отображения для {} с id {} не найдено. Применяем дефолтное", type.name(), uuid);
            return List.of(ImageResponseDto.builder()
                    .id(UUID.randomUUID())
                    .image(defaultImage)
                    .build());
        }

        return (List<ImageResponseDto>) objectMapper.convertCollection(result, ImageResponseDto.class);
    }

    @Transactional
    public List<ImageResponseDto> getDeviceImages(String uuid){
        var device = deviceRepository.getDeviceBySerial(UUID.fromString(uuid))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DEVICE, uuid));

        List<Image> images;

        if(Objects.nonNull(device.getGroup()) && Boolean.TRUE.equals(device.getGroup().getIsActive())){
            images = device.getGroup().getImages();
            if(CollectionUtils.isNotEmpty(images)){
                log.info("Выводим изображение по группе {} для девайса {}", device.getGroup().getName(), uuid);
                return (List<ImageResponseDto>) objectMapper.convertCollection(images, ImageResponseDto.class);
            }
        }

        if(Objects.nonNull(device.getSalePoint())){
            images = device.getSalePoint().getImages();
            if(CollectionUtils.isNotEmpty(images)){
                log.info("Выводим изображение по SalePoint {} для девайса {}", device.getSalePoint().getName(), uuid);
                return (List<ImageResponseDto>) objectMapper.convertCollection(images, ImageResponseDto.class);
            }
        }

        images = device.getImages();
        if(CollectionUtils.isNotEmpty(images)){
            log.info("Выводим изображение по данным девайся {}", uuid);
            return (List<ImageResponseDto>) objectMapper.convertCollection(images, ImageResponseDto.class);
        }


        log.info("Изображение для отображения на устройстве {} не найдено. Применяем дефолтное", uuid);
        return List.of(ImageResponseDto.builder()
                .id(UUID.randomUUID())
                .image(defaultImage)
                .build());
    }

    @Transactional
    public List<Image> getActiveImages(String cname){
        return companyService.getSafeCompanyByCname(cname)
                .getImages().stream()
                .filter(image -> activeImagesOnGroup(image.getDevGroups())
                || !CollectionUtils.isEmpty(image.getSalePoints()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Image> getCreatedImages(String cname){
        return companyService.getSafeCompanyByCname(cname)
                .getImages();
    }

    public boolean activeImagesOnGroup(List<Group> group){
        if(CollectionUtils.isEmpty(group)){
            return false;
        }
        return group.stream().anyMatch(Group::getIsActive);
    }

    @Transactional
    public List<Image> getAllImages(String cname) {
        return companyService.getSafeCompanyByCname(cname)
                .getImages();
    }

    public Image getCreatedImage(String id) {
        return repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.IMAGE, id));
    }
}
