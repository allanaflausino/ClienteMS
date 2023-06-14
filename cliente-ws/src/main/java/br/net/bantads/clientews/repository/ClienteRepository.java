package br.net.bantads.clientews.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import br.net.bantads.clientews.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    public Cliente findByCpf(String cpf);
    public Cliente findById(Integer id);
}

