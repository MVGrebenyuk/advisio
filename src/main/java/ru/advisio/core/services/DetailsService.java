package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.details.DetailsUpdateDto;
import ru.advisio.core.dto.details.DetailsResponseDto;
import ru.advisio.core.entity.Company;
import ru.advisio.core.entity.Details;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.DetailsRepository;
import ru.advisio.core.utils.CollectionObjectMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailsService {

    private final static String DEFAULT_NAME = "Unnamed company";

    private final DetailsRepository detailsRepository;
    private final CollectionObjectMapper objectMapper;

    public Details createForNewAccount(Company company){
        return detailsRepository.save(Details.builder()
                        .id(UUID.randomUUID())
                        .company(company)
                        .officialName(DEFAULT_NAME)
                .build());
    }

    @Transactional
    public DetailsResponseDto enrich(DetailsUpdateDto detailsUpdateDto){
        var entity = detailsRepository.findByCompanyId(UUID.fromString(detailsUpdateDto.getAccountId()))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.DETAILS, "(AccountId) " + detailsUpdateDto.getAccountId()));

        entity.setOfficialName(detailsUpdateDto.getOfficialName());
        return (DetailsResponseDto) objectMapper.convertValue(entity, DetailsResponseDto.class);
    }

}
