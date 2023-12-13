package com.sberStart.bank_api.service.admin;

import com.sberStart.bank_api.dto.admin.ClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.ClientAdminResponseDTO;
import com.sberStart.bank_api.dto.admin.DataClientResponseDTO;
import com.sberStart.bank_api.dto.admin.NewClientAdminRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountRequestDTO;
import com.sberStart.bank_api.dto.admin.OpenAccountResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AdminBankService {
    ClientAdminResponseDTO createNewClient(NewClientAdminRequestDTO client);

    ClientAdminResponseDTO updateClient(ClientAdminRequestDTO client);

    DataClientResponseDTO getDataClient(UUID idClient);

    OpenAccountResponseDTO openAccountByIdClient(OpenAccountRequestDTO openAccount);
}
