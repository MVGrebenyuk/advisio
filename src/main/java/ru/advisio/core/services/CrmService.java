package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.crm.ConnectionCrmDto;
import ru.advisio.core.dto.crm.CrmDto;
import ru.advisio.core.entity.crm.ConnectionCrm;
import ru.advisio.core.entity.crm.ConnectionCrmRepository;
import ru.advisio.core.entity.crm.Crm;
import ru.advisio.core.enums.CrmType;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CrmRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CrmService {
    private final ConnectionCrmRepository connectionCrmRepository;
    private final CrmRepository crmRepository;
    private final CompanyService companyService;

    public CrmDto create(String cname, CrmDto crmDto) {
        var crm = saveOrUpdateCrm(cname, crmDto);

        if(!crmDto.getCrmType().equals(CrmType.LOCAL)){
            var connection = ConnectionCrm.builder()
                            .id(UUID.randomUUID())
                            .crm(crm)
                            .connectionName(crmDto.getConnectionParams().getConnectionName())
                            .connectionUrl(crmDto.getConnectionParams().getConnectionUrl())
                            .login(crmDto.getConnectionParams().getLogin())
                            .password(crmDto.getConnectionParams().getPassword())
                            .token(crmDto.getConnectionParams().getToken())
                            .connectionType(crmDto.getConnectionParams().getConnectionType())
                    .build();

            connectionCrmRepository.save(connection);
            crm.setConnectionCrm(connection);
        }

        return CrmDto.builder()
                .id(crm.getId())
                .crmType(crm.getCrmType())
                .name(crm.getName())
                .build();
    }

    @Transactional
    public Crm saveOrUpdateCrm(String cname, CrmDto crmDto) {
        var company = companyService.getSafeCompanyByCname(cname).getId();

        return crmRepository.save(Crm.builder()
                        .id(UUID.randomUUID())
                        .companyId(company)
                        .crmType(crmDto.getCrmType())
                        .name(crmDto.getName())
                .build());
    }

    public CrmDto test(String cname, CrmDto crmDto) {
        return null;
    }

    public CrmDto updateConnection(String cname, ConnectionCrmDto crmDto) {
        return null;
    }

    public List<CrmDto> getCompanyCrms(String cname) {
       return crmRepository.findAllByCompanyId(companyService.getSafeCompanyByCname(cname).getId())
                .stream().map(crm -> CrmDto.builder()
                        .id(crm.getId())
                        .crmType(crm.getCrmType())
                        .name(crm.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public CrmDto getCompanyCrm(UUID crmId) {
        var crm =  crmRepository.findById(crmId)
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.CRM, crmId));

        return CrmDto.builder()
                .id(crm.getId())
                .crmType(crm.getCrmType())
                .name(crm.getName())
                .build();
    }

    public ConnectionCrmDto getCrmConnection(UUID crmId) {
        var connection = crmRepository.findById(crmId)
                .orElseThrow(() -> new AdvisioEntityNotFound(EnType.CRM, crmId))
                .getConnectionCrm();

        return ConnectionCrmDto.builder()
                .crmType(connection.getCrmType())
                .connectionName(connection.getConnectionName())
                .connectionType(connection.getConnectionType())
                .connectionUrl(connection.getConnectionUrl())
                .password(connection.getPassword())
                .login(connection.getLogin())
                .token(connection.getToken())
                .build();
    }
}
