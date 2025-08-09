package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.salepoints.CreateSalePointDto;
import ru.advisio.core.dto.salepoints.SalePointDto;
import ru.advisio.core.dto.salepoints.UpdateSalePointDto;
import ru.advisio.core.entity.Device;
import ru.advisio.core.entity.Image;
import ru.advisio.core.entity.SalePoint;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;
import ru.advisio.core.repository.SalePointRepository;
import ru.advisio.core.utils.CollectionObjectMapper;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalePointsService {

    private final SalePointRepository repository;
    private final CompanyRepository companyRepository;
    private final CollectionObjectMapper mapper;

    @Transactional
    public SalePointDto createSalesPoint(String cname, CreateSalePointDto salePointDto) {
        log.info("Request to create salepoint for {} : {}", cname, salePointDto.getName());
        var res = repository.save(new SalePoint().builder()
                        .id(UUID.randomUUID())
                        .company(companyRepository.findByCname(cname)
                                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.COMPANY, cname)
                        ))
                        .name(salePointDto.getName())
                .build());
        log.info("Sales point successfull created");

        return (SalePointDto) mapper.convertValue(res, SalePointDto.class);
    }

    public SalePointDto getSalesPoint(String cname, String spId) {
        log.info("Get sale point with id {} by {}", spId, cname);
        return (SalePointDto) mapper.convertValue(repository.findById(UUID.fromString(spId))
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, spId)), SalePointDto.class);
    }

    public List<SalePointDto> getAllSalesPointsByCname(String cname) {
        log.info("Get salesPoint by {}", cname);
        return (List<SalePointDto>) mapper.convertCollection(repository.findAllByCompany_CnameIgnoreCase(cname), SalePointDto.class);
    }

    @Transactional
    public SalePointDto editSalePoint(String cname, String spId, UpdateSalePointDto dto) {
        var entity = repository.findById(UUID.fromString(spId)).orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, spId));
        entity.setName(dto.getUpdatedName());

        return (SalePointDto) mapper.convertValue(entity, SalePointDto.class);
    }

    public List<Image> getAllImagesBySp(String cname, String spId) {
        return repository.findById(UUID.fromString(spId)).orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, spId))
                .getImages();
    }

    public List<Device> getAllDevicesBySp(String cname, String spId) {
        return repository.findById(UUID.fromString(spId)).orElseThrow(() -> new AdvisioEntityNotFound(EnType.SP, spId))
                .getDevices();
    }
}
