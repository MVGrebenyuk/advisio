package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.account.CompanyRegisterDto;
import ru.advisio.core.dto.account.CompanyResponseDto;
import ru.advisio.core.entity.Company;
import ru.advisio.core.exceptions.AdvisioAccountExistException;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.utils.CollectionObjectMapper;
import ru.advisio.core.utils.UserUtil;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private final DetailsService detailsService;
    private final CompanyGroupService groupService;
    private final CollectionObjectMapper<CompanyResponseDto, Company> collectionObjectMapper;

    @Transactional
    public CompanyResponseDto register(Principal principal, CompanyRegisterDto requestDto){
        log.info("Регистрация компании: {}", requestDto);

        if(repository.existsCompanyByCname(requestDto.getCname())){
            throw new AdvisioAccountExistException(requestDto.getCname());
        }

        var savedEntity = repository.save(Company.builder()
                .id(UUID.randomUUID())
                .companyType(requestDto.getCompanyType())
                .cname(requestDto.getCname())
                .build());

        savedEntity.setDetails(detailsService.createForNewAccount(savedEntity));
        groupService.createMainGroup(principal.getName(), requestDto.getCname());

        return collectionObjectMapper.convertValue(savedEntity, CompanyResponseDto.class);
    }

    public Set<CompanyResponseDto> getUserCompanyes() {
        return collectionObjectMapper.convertCollection(
                repository.findCompaniesByCnameIn(
                        UserUtil.getOidcUserCompanyes()), CompanyResponseDto.class);
    }

}
