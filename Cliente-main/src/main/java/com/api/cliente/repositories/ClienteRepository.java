package com.api.cliente.repositories;

import com.api.cliente.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<ClienteModel> findByEmail(String email);
    Optional<ClienteModel> findByCpf(String cpf);
    boolean existsByTelefone(String telefone);
}
