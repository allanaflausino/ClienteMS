package com.api.cliente.services;

import com.api.cliente.models.ClienteModel;
import com.api.cliente.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {
    final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteModel save(ClienteModel clienteModel) {
        return clienteRepository.save(clienteModel);
    }

    public Page<ClienteModel> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Optional<ClienteModel> findById(UUID id) {
        return clienteRepository.findById(id);
    }

    public Optional<ClienteModel> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<ClienteModel> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Transactional
    public void delete(ClienteModel clienteModel) {
        clienteRepository.delete(clienteModel);
    }

    public boolean existsByCpf(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    public boolean existsByTelefone(String telefone) {
        return clienteRepository.existsByTelefone(telefone);
    }

    public ClienteModel inserirCliente(ClienteModel clienteModel) {
        return clienteRepository.save(clienteModel);
    }
}
