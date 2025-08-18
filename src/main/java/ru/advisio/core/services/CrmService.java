package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.advisio.core.dto.crm.ConnectionCrmDto;
import ru.advisio.core.dto.crm.CrmDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrmService {
    public CrmDto create(String cname, CrmDto crmDto) {
        return null;
    }

    public CrmDto test(String cname, CrmDto crmDto) {
        return null;
    }

    public CrmDto updateConnection(String cname, ConnectionCrmDto crmDto) {
        return null;
    }
}
