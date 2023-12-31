package com.api.cliente.controllers;

import com.api.cliente.dtos.ClienteDto;
import com.api.cliente.models.ClienteModel;
import com.api.cliente.repositories.ClienteRepository;
import com.api.cliente.services.ClienteService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cliente")
public class ClienteController {
    final ClienteService clienteService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<Page<ClienteModel>> getAllClientes(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getClienteById(@PathVariable(value = "id") UUID id){
        Optional<ClienteModel> clienteModelOptional = clienteService.findById(id);
        if (!clienteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModelOptional.get());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Object> getClienteByEmail(@PathVariable(value = "email") String email){
        Optional<ClienteModel> clienteModelOptional = clienteService.findByEmail(email);
        if (!clienteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModelOptional.get());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Object> getClienteByCpf(@PathVariable(value = "cpf") String cpf){
        Optional<ClienteModel> clienteModelOptional = clienteService.findByCpf(cpf);
        if (!clienteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModelOptional.get());
    }
    @PostMapping
    public ResponseEntity<Object> inserirCliente(@RequestBody ClienteModel cliente) throws JsonProcessingException {
        Optional<ClienteModel> clienteExistente = clienteService.findByCpf(cliente.getCpf());
        if (clienteExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um cliente com o CPF informado.");
        }
        ClienteModel novoCliente = clienteService.inserirCliente(cliente);

        var jsonCliente = objectMapper.writeValueAsString(cliente);
        rabbitTemplate.convertAndSend("CLIENTE", jsonCliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }
}
