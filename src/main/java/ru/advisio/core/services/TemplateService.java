package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.advisio.core.dto.template.TemplateDto;
import ru.advisio.core.entity.Template;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.TemplateRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TemplateService {

    private final static String UNNAMED = "Без названия";
    private final static Integer DEF_PAGE_SIZE = 9;

    private final TemplateRepository repository;
    private final CompanyService companyService;
    private final AwsService awsService;

    public TemplateDto uploadTemplate(String cname, MultipartFile file) {
        log.info("Сохраняем шаблон для компании {}", cname);
        var responseImageUrl = awsService.uploadImage(file);
        var entity = repository.save(Template.builder()
                .id(UUID.randomUUID())
                .name(UNNAMED)
                .url(responseImageUrl.getImage())
                .company(companyService.getSafeCompanyByCname(cname))
                .creationDt(LocalDateTime.now())
                .build());
        return TemplateDto.builder()
                .id(entity.getId().toString())
                .url(entity.getUrl())
                .dateOfLoad(entity.getCreationDt())
                .build();
    }

    public Page<TemplateDto> getAllTemplates(String cname, Integer pageNumber, Integer pageSize) {
        if(pageNumber == null){
            pageNumber = 0;
        }

        if(pageSize == null){
            pageSize = DEF_PAGE_SIZE;
        }

        var templates = repository.findAllByCompany_Cname(cname, PageRequest.of(pageNumber, pageSize));
        var dtosList = templates.stream().map(entity -> TemplateDto.builder()
                .id(entity.getId().toString())
                .url(entity.getUrl())
                .dateOfLoad(entity.getCreationDt())
                .build())
                .collect(Collectors.toList());
        return new PageImpl<>(dtosList, PageRequest.of(pageNumber, pageSize), dtosList.size());
    }

    public TemplateDto getTempalteById(String id) {
        var entity = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.TEMPLATE, id));
        return TemplateDto.builder()
                .id(entity.getId().toString())
                .url(entity.getUrl())
                .dateOfLoad(entity.getCreationDt())
                .build();
    }

    public Boolean deleteTemplate(String id) {
        repository.deleteById(UUID.fromString(id));
        return true;
    }

    public TemplateDto updateTemplate(String id, String name) {
        var entity = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.TEMPLATE, id));
        entity.setName(name);

        return TemplateDto.builder()
                .id(entity.getId().toString())
                .url(entity.getUrl())
                .dateOfLoad(entity.getCreationDt())
                .build();
    }
}
