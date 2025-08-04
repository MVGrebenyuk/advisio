package ru.advisio.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.advisio.core.dto.account.AccountRegisterDto;
import ru.advisio.core.dto.account.AccountResponseDto;
import ru.advisio.core.entity.Account;
import ru.advisio.core.exceptions.AdvisioAccountExistException;
import ru.advisio.core.repository.AccountRepository;
import ru.advisio.core.utils.CollectionObjectMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final DetailsService detailsService;
    private final CollectionObjectMapper<AccountResponseDto, Account> collectionObjectMapper;

    @Transactional
    public AccountResponseDto register(AccountRegisterDto requestDto){
        log.info("Регистрация аккаунта: {}", requestDto);

        if(repository.existsByEmailOrPhone(requestDto.getEmail(), requestDto.getPhone())){
            throw new AdvisioAccountExistException(requestDto.getEmail(), requestDto.getPhone());
        }

        var savedEntity = repository.save(Account.builder()
                .id(UUID.randomUUID())
                .email(requestDto.getEmail())
                .companyType(requestDto.getCompanyType())
                .phone(requestDto.getPhone())
                .build());

        savedEntity.setDetails(detailsService.createForNewAccount(savedEntity));
        return collectionObjectMapper.convertValue(savedEntity, AccountResponseDto.class);
    }

}
