package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.entity.Tag;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.repository.TagRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repository;
    private final CompanyRepository companyRepository;

    public List<Tag> getTags(String cname){
        return repository.getTagsByCompany_Cname(cname);
    }

    @Transactional
    public Tag createTag(String cname, String tagName){
        return repository.save(Tag.builder()
                        .id(UUID.randomUUID())
                        .name(tagName)
                        .company(companyRepository.findByCname(cname)
                                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.COMPANY, cname)))
                .build());
    }

    public int deleteTag(String tagName){
        return repository.deleteTagByName(tagName);
    }

    public int deleteTag(UUID id){
        return repository.deleteTagById(id);
    }

}
