package com.sberStart.bank_api.service.admin;

import com.sberStart.bank_api.dto.admin.ClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.ClientAdminResponseDTO;
import com.sberStart.bank_api.dto.admin.DataClientResponseDTO;
import com.sberStart.bank_api.dto.admin.NewClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountResponseDTO;
import com.sberStart.bank_api.entity.AccountBankEntity;
import com.sberStart.bank_api.entity.ClientBankEntity;
import com.sberStart.bank_api.entity.ClientHistoryBankEntity;
import com.sberStart.bank_api.exception.BankException;
import com.sberStart.bank_api.repository.AccountBankRepository;
import com.sberStart.bank_api.repository.ClientBankRepository;
import com.sberStart.bank_api.repository.ClientHistoryBankRepository;
import com.sberStart.bank_api.service.GenerateNumberAccountBankService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminBankServiceImp implements AdminBankService {
    private final ClientBankRepository clientBankRepository;
    private final ClientHistoryBankRepository clientHistoryBankRepository;
    private final AccountBankRepository accountBankRepository;
    private final ModelMapper mapper;
    private final GenerateNumberAccountBankService generateNumberAccount;


    /**
     * создание нового клиента
     *
     * @param client это информация о клиенте, содержащая id, firstName, lastName, phoneNumber, email
     * @return сущность, содержащую  id, firstName, lastName, phoneNumber, email, createdTime, modificationTime
     */
    @Transactional
    @Override
    public ClientAdminResponseDTO createNewClient(NewClientAdminRequestDTO client) {
        ClientBankEntity clientBankEntity = ClientBankEntity.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .email(client.getEmail())
                .createdTime(LocalDateTime.now())
                .build();
        //clientBankRepository.save(mapper.map(clientBankEntity, ClientBankEntity.class));
        clientBankEntity = clientBankRepository.save(clientBankEntity);
        return mapper.map(clientBankEntity, ClientAdminResponseDTO.class);
    }

    /**
     * изменение данных профиля клиента, при этом предудущие данные хранятся в истории с указанием времени их изменения
     *
     * @param client это информация о клиенте, содержащая id, firstName, lastName, phoneNumber, email
     * @return обновленную сущность, содержащую  id, firstName, lastName, phoneNumber, email, createdTime, modificationTime
     * @throws BankException 404, если клиент не найден в БД
     */
    @Transactional
    @Override
    public ClientAdminResponseDTO updateClient(ClientAdminRequestDTO client) {
        Optional<ClientBankEntity> existClientOptional = clientBankRepository.findById(client.getId());
        ClientBankEntity existClient = existClientOptional
                .orElseThrow(() -> new BankException("ID_USER " + client.getId() + " не найдено в БД"));
        ClientHistoryBankEntity clientHistoryBankEntity = ClientHistoryBankEntity.builder()
                .client(ClientBankEntity.builder()
                        .id(client.getId())
                        .build())
                .firstName(existClient.getFirstName())
                .lastName(existClient.getLastName())
                .email(existClient.getEmail())
                .phoneNumber(existClient.getPhoneNumber())
                .createdTime(existClient.getCreatedTime())
                .modificationTime(LocalDateTime.now())
                .build();
        clientHistoryBankRepository.save(clientHistoryBankEntity);
        ClientBankEntity updateClientBankEntity = ClientBankEntity.builder()
                .id(client.getId())
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .createdTime(existClient.getCreatedTime())
                .modificationTime(LocalDateTime.now())
                .build();

        clientBankRepository.save(updateClientBankEntity);
        return mapper.map(updateClientBankEntity, ClientAdminResponseDTO.class);
    }

    /**
     * вывод всех данных о клиенте по его id
     *
     * @param idClient id клиента, по которому нужно вернуть информацию
     * @return сущность, содержащую id, firstName, lastName, phoneNumber, email, accountList - список счетов
     * @throws BankException возвращает 404, если такой клиент не найден в БД
     */
    @Transactional
    @Override
    public DataClientResponseDTO getDataClient(UUID idClient) {
        Optional<ClientBankEntity> clientBankEntityOptional = clientBankRepository.findById(idClient);
        ClientBankEntity clientBankEntity = clientBankEntityOptional
                .orElseThrow(() -> new BankException("ID_USER " + idClient + " не найдено в БД"));
        return mapper.map(clientBankEntity, DataClientResponseDTO.class);
    }

    /**
     * открытие нового счета у клиента по id
     *
     * @param openAccount сущность, содержащая id клиента и numberAccount(номера счета, который нужно создать)
     * @return сущность, содержающую id нового счета и его номер numberAccount
     * @throws BankException 404, если клиент не найден в БД
     */
    @Transactional
    @Override
    public OpenAccountResponseDTO openAccountByIdClient(OpenAccountRequestDTO openAccount) {
        clientBankRepository.findById(openAccount.getId())
                .orElseThrow(() -> new BankException("ID_USER " + openAccount.getId() + " не найдено в БД"));
        AccountBankEntity accountBankEntity = AccountBankEntity.builder()
                .balance(new BigDecimal(0))
                .status(openAccount.getStatus())
                .currency(openAccount.getCurrency())
                .numberAccount(generateNumberAccount.generateNumberAccount())
                .userBank(ClientBankEntity.builder()
                        .id(openAccount.getId())
                        .build())
                .build();
        accountBankRepository.save(accountBankEntity);
        return mapper.map(accountBankEntity, OpenAccountResponseDTO.class);
    }
}
