package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.advisio.core.dto.crm.ConnectionCrmDto;
import ru.advisio.core.dto.crm.CrmDto;
import ru.advisio.core.entity.crm.ConnectionCrm;
import ru.advisio.core.entity.crm.Crm;
import ru.advisio.core.enums.CrmType;
import ru.advisio.core.repository.CrmRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrmService {
    private final CrmRepository crmRepository;
    private final CompanyService companyService;

    public CrmDto create(String cname, CrmDto crmDto) {
        var crm = crmRepository.save(Crm.builder()
                        .id(UUID.randomUUID())
                        .companyId(companyService.getSafeCompanyByCname(cname).getId())
                        .crmType(crmDto.getCrmType())
                        .name(crmDto.getName())
                .build());

        if(!crmDto.getCrmType().equals(CrmType.LOCAL)){
            crm.setConnectionCrm(ConnectionCrm.builder()
                            .id(UUID.randomUUID())
                            .crm(crm)
                            .connectionName(crmDto.getConnectionParams().getConnectionName())
                            .connectionUrl(crmDto.getConnectionParams().getConnectionUrl())
                            .login(crmDto.getConnectionParams().getLogin())
                            .password(crmDto.getConnectionParams().getPassword())
                            .token(crmDto.getConnectionParams().getToken())
                            .connectionType(crmDto.getConnectionParams().getConnectionType())
                    .build());
        }

        crmRepository.save(crm);
        return CrmDto.builder()
                .id(crm.getId())
                .crmType(crm.getCrmType())
                .name(crm.getName())
                .build();
    }

    public CrmDto test(String cname, CrmDto crmDto) {
        return null;
    }

    public CrmDto updateConnection(String cname, ConnectionCrmDto crmDto) {
        return null;
    }
}
