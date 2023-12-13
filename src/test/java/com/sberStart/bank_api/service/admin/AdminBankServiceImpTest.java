package com.sberStart.bank_api.service.admin;

import com.sberStart.bank_api.dto.admin.ClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.ClientAdminResponseDTO;
import com.sberStart.bank_api.dto.admin.DataClientResponseDTO;
import com.sberStart.bank_api.dto.admin.NewClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountResponseDTO;
import com.sberStart.bank_api.entity.ClientBankEntity;
import com.sberStart.bank_api.repository.AccountBankRepository;
import com.sberStart.bank_api.repository.ClientBankRepository;
import com.sberStart.bank_api.repository.ClientHistoryBankRepository;
import com.sberStart.bank_api.service.GenerateNumberAccountBankService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminBankServiceImpTest {
    @InjectMocks
    AdminBankServiceImp adminBankService;
    @Mock
    private ClientBankRepository clientBankRepository;
    @Mock
    private ClientHistoryBankRepository clientHistoryBankRepository;
    @Mock
    private AccountBankRepository accountBankRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private GenerateNumberAccountBankService generateNumberAccount;

    @Test
    @DisplayName("Создание нового клиента")
    public void createNewClientTest() {
        UUID idClient = UUID.randomUUID();
        var clientAdminResponseDTOExpected = ClientAdminResponseDTO.builder()
                .id(idClient)
                .firstName("Вася")
                .lastName("Петров")
                .email("test@mail.ru")
                .phoneNumber("1258")
                .build();
        var saveEntity = ClientBankEntity.builder()
                .id(idClient)
                .firstName("Вася")
                .lastName("Петров")
                .email("test@mail.ru")
                .phoneNumber("1258")
                .createdTime(LocalDateTime.now())
                .build();
        var requestDTO = NewClientAdminRequestDTO.builder()
                .firstName("Вася")
                .lastName("Петров")
                .email("test@mail.ru")
                .phoneNumber("1258")
                .build();
        when(clientBankRepository.save(any(ClientBankEntity.class))).thenAnswer(inv -> inv.getArguments()[0]);
        when(mapper.map(any(ClientBankEntity.class), any())).thenReturn(clientAdminResponseDTOExpected);

        var resultDTO = adminBankService.createNewClient(requestDTO);
        assertNotNull(resultDTO);
    }

    @Test
    @DisplayName("изменение данных профиля клиента")
    public void updateClientTest() {
        UUID idClient = UUID.randomUUID();
        var clientAdminResponseDTOExpected = ClientAdminResponseDTO.builder()
                .id(idClient)
                .firstName("Вася")
                .lastName("Петров")
                .email("test@mail.ru")
                .phoneNumber("1258")
                .build();
        var clientBank = ClientBankEntity.builder()
                .id(idClient)
                .firstName("Петя")
                .lastName("Петров")
                .email("test@mail.ru")
                .phoneNumber("1258")
                .build();
        var clientAdminRequestDTO = ClientAdminRequestDTO.builder()
                .id(idClient)
                .build();
        when(clientBankRepository.findById(idClient)).thenReturn(Optional.of(clientBank));
        when(mapper.map(any(), eq(ClientAdminResponseDTO.class))).thenReturn(clientAdminResponseDTOExpected);
        ClientAdminResponseDTO clientAdminResponseDTOActual = adminBankService.updateClient(clientAdminRequestDTO);
        assertEquals(clientAdminResponseDTOExpected, clientAdminResponseDTOActual);
    }

    @Test
    @DisplayName("Вывод всех данных о клиенте по его id")
    public void getDataClientTest() {
        UUID idClient = UUID.randomUUID();
        var expected = DataClientResponseDTO.builder()
                .id(idClient)
                .firstName("Тест")
                .lastName("Тест")
                .phoneNumber("889784")
                .email("test@mail.ru")
                .accountList(new ArrayList<>())
                .build();
        var clientBank = ClientBankEntity.builder()
                .id(idClient)
                .firstName("Петя")
                .lastName("Петров")
                .email("test@mail.ru")
                .phoneNumber("1258")
                .build();
        when(clientBankRepository.findById(idClient)).thenReturn(Optional.of(clientBank));
        when(mapper.map(any(), eq(DataClientResponseDTO.class))).thenReturn(expected);
        var actual = adminBankService.getDataClient(idClient);
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Открытие нового счета у клиента по id")
    public void openAccountByIdClientTest() {
        UUID idClient = UUID.randomUUID();
        UUID idAccount = UUID.randomUUID();
        var expected = OpenAccountResponseDTO.builder()
                .id(idAccount)
                .numberAccount("1234567897894561")
                .build();
        var clientBank = ClientBankEntity.builder()
                .id(idClient)
                .build();
        var openAccountRequestDTO = OpenAccountRequestDTO.builder()
                .id(idClient)
                .numberAccount("1234567897894561")
                .status(true)
                .currency("RUB")
                .build();
        when(clientBankRepository.findById(idClient)).thenReturn(Optional.of(clientBank));
        when(mapper.map(any(), eq(OpenAccountResponseDTO.class))).thenReturn(expected);
        var actual = adminBankService.openAccountByIdClient(openAccountRequestDTO);
        assertEquals(expected, actual);
    }
}
