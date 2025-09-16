package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.media.MediaDto;
import ru.advisio.core.entity.Image;
import ru.advisio.core.repository.ImageRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final ImageService imageService;

    @Transactional
    public Page<MediaDto> getAllActiveMedia(String cname, Integer pageNumber, Integer pageSize) {
        List<Image> activeImages = imageService.getActiveImages(cname);

        if(!activeImages.isEmpty() && activeImages.size() >= (pageSize * pageNumber) + pageSize) {
               activeImages = activeImages.subList(pageSize * pageNumber, (pageSize * pageNumber) + pageSize);
        }

        return getMediaDtos(pageNumber, pageSize, activeImages, true);
    }

    @Transactional
    public Page<MediaDto> getAllCreatedMedia(String cname, Integer pageNumber, Integer pageSize) {
        List<Image> activeImages = imageService.getActiveImages(cname);

        if(!activeImages.isEmpty() && activeImages.size() >= (pageSize * pageNumber) + pageSize) {
            activeImages = activeImages.subList(pageSize * pageNumber, (pageSize * pageNumber) + pageSize);
        }

        return getMediaDtos(pageNumber, pageSize, activeImages, null);
    }

    private Page<MediaDto> getMediaDtos(Integer pageNumber, Integer pageSize,
                                        List<Image> activeImages, Boolean active) {
        if(CollectionUtils.isEmpty(activeImages)){
            return Page.empty();
        }

        List<MediaDto> medias = activeImages.stream()
                .map(image -> MediaDto.builder()
                        .id(image.getId().toString())
                        .url(image.getImage())
                        .name(image.getTemplate().getName())
                        .activeDevices(MediaDto.ImageActiveDevices.builder()
                                .map(mapToActiveDevicesAndSalePoints(image))
                                .build())
                        .template(MediaDto.ShortTemplateDto.builder()
                                .uuid(image.getTemplate().getId().toString())
                                .name(image.getTemplate().getName())
                                .build())
                        .isActive(active != null ? active : imageService.activeImagesOnGroup(image.getDevGroups())
                                || !CollectionUtils.isEmpty(image.getSalePoints()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(medias, PageRequest.of(pageNumber, pageSize), activeImages.size());
    }

    private Map<String, Set<MediaDto.ShortDeviceDto>> mapToActiveDevicesAndSalePoints(Image image) {
        Map<String, Set<MediaDto.ShortDeviceDto>> map = new HashMap<>();

        image.getSalePoints().forEach(sp -> {
            var devices = sp.getDevices()
                    .stream()
                    .map(device -> MediaDto.ShortDeviceDto
                            .builder()
                            .id(device.getId().toString())
                            .id(device.getSerial().toString())
                            .build())
                    .collect(Collectors.toSet());
            if(map.containsKey(sp.getName())){
                map.get(sp.getName()).addAll(devices);
            } else {
                map.put(sp.getName(), devices);
            }
        });

        image.getDevGroups()
                .forEach(group -> group.getDevices()
                        .forEach(device -> {
                            var spName = device.getSalePoint().getName();
                            var shortDevice = MediaDto.ShortDeviceDto
                                    .builder()
                                    .id(device.getId().toString())
                                    .name(device.getSerial().toString())
                                    .build();
                            if(map.containsKey(spName)){
                                map.get(spName).add(shortDevice);
                            } else {
                                map.put(spName, new HashSet<>(Arrays.asList(shortDevice)));
                            }
                        }));
        return map;
    }
}
