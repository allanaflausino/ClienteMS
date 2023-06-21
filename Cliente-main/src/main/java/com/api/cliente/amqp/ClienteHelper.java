package com.api.cliente.amqp;

import com.api.cliente.dtos.ClienteDto;
import com.api.cliente.models.ClienteModel;
import com.api.cliente.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClienteHelper {
    final ClienteService clienteService;

    public ClienteHelper(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public ResponseEntity<String> saveCliente(@Valid ClienteDto clienteDto){
        if(clienteService.existsByCpf(clienteDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O CPF já está sendo utilizado!");
        }
        if(clienteService.existsByEmail(clienteDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O e-mail já está sendo utilizado!");
        }
        if(clienteService.existsByTelefone(clienteDto.getTelefone())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O telefone já está sendo utilizada!");
        }

        var clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDto, clienteModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(clienteModel).getId().toString());
    }

    public ResponseEntity<String> updateCliente(UUID id, @Valid ClienteDto clienteDto){
        if(clienteService.existsByCpf(clienteDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O CPF já está sendo utilizado!");
        }
        if(clienteService.existsByEmail(clienteDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O e-mail já está sendo utilizado!");
        }
        if(clienteService.existsByTelefone(clienteDto.getTelefone())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O telefone já está sendo utilizada!");
        }

        Optional<ClienteModel> clienteModelOptional = clienteService.findById(id);
        if (!clienteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        var clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDto, clienteModel);
        clienteModel.setId(clienteModelOptional.get().getId());

        clienteService.save(clienteModel);
        return ResponseEntity.status(HttpStatus.OK).body(clienteModelOptional.get().getEmail());
    }

    public ResponseEntity<ClienteDto> deleteCliente(UUID id){
        Optional<ClienteModel> clienteModelOptional = clienteService.findById(id);
        if (!clienteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClienteDto());
        }

        ClienteDto clienteDto = new ClienteDto();

        BeanUtils.copyProperties(clienteModelOptional.get(), clienteDto);

        clienteService.delete(clienteModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(clienteDto);
    }
}
