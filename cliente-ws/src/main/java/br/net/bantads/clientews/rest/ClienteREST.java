package br.net.bantads.clientews.rest;


import br.net.bantads.clientews.dto.ClienteDTO;
import br.net.bantads.clientews.model.Cliente;
import br.net.bantads.clientews.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class ClienteREST {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/cliente")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<Cliente> lista = clienteRepository.findAll();
        Collections.sort(lista);
        return ResponseEntity.ok().body(lista.stream().map(g -> mapper.map(g, ClienteDTO.class)).collect(Collectors.toList()));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<ClienteDTO> buscarCliente(@PathVariable("id") int id) {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente != null) {
            return ResponseEntity.status(200).body(mapper.map(cliente, ClienteDTO.class));
        }
        return ResponseEntity.status(404).build();
    }

    @PostMapping("/cliente")
    public ResponseEntity<ClienteDTO> inserirCliente(@RequestBody ClienteDTO cliente) {
        if (clienteRepository.findByCpf(cliente.getCpf()) == null) {
            clienteRepository.save(mapper.map(cliente, Cliente.class));
            Cliente ger = clienteRepository.findByCpf(cliente.getCpf());
            return ResponseEntity.status(201).body(mapper.map(ger, ClienteDTO.class));
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/cliente")
    public ResponseEntity<ClienteDTO> alterarGerente(@RequestBody ClienteDTO cliente) {
        Cliente ger = clienteRepository.findById(cliente.getId());
        if (ger != null) {
            ger.setNome(cliente.getNome());
            ger.setEmail(cliente.getEmail());
            ger.setTelefone(cliente.getTelefone());
            clienteRepository.save(ger);
            return ResponseEntity.status(200).body(mapper.map(ger, ClienteDTO.class));
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<ClienteDTO> removerCliente(@PathVariable("id") int id) {
        List<Cliente> lista = clienteRepository.findAll();
        Cliente cliente = lista.stream().filter(g -> g.getId() == id).findAny().orElse(null);
        if (cliente != null) {
            clienteRepository.delete(cliente);
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }



}
