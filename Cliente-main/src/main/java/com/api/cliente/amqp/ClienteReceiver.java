package com.api.cliente.amqp;

import com.api.cliente.dtos.ClienteDto;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Objects;
import java.util.UUID;

@RabbitListener(queues = "cliente")
public class ClienteReceiver {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private ClienteProducer clienteProducer;
    @Autowired
    private ClienteHelper clienteHelper;

    @RabbitHandler
    public ClienteTransfer receive(@Payload ClienteTransfer clienteTransfer) {
        if (clienteTransfer.getAction().equals("save-cliente")) {
            if (Objects.isNull(clienteTransfer.getClienteDto().getCpf())) {
                clienteTransfer.setAction("failed-cliente");
                clienteTransfer.setMessage(("Nenhum dado de Cliente foi passado."));
                return clienteTransfer;
            }

            ResponseEntity<String> response = clienteHelper.saveCliente(clienteTransfer.getClienteDto());

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                clienteTransfer.setAction("success-cliente");
                clienteTransfer.setMessage(response.getBody());
                return clienteTransfer;
            }

            clienteTransfer.setAction("failed-cliente");
            clienteTransfer.setMessage(Objects.requireNonNull(response.getBody()));
            return clienteTransfer;
        }

        if (clienteTransfer.getAction().equals("update-cliente")) {
            if (Objects.isNull(clienteTransfer.getClienteDto().getCpf())) {
                clienteTransfer.setAction("failed-cliente");
                clienteTransfer.setMessage(("Nenhum dado de Cliente foi passado."));
                return clienteTransfer;
            }

            UUID idUpdate = UUID.fromString(clienteTransfer.getMessage());

            ResponseEntity<String> response = clienteHelper.updateCliente(idUpdate, clienteTransfer.getClienteDto());

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                clienteTransfer.setAction("success-cliente");
                clienteTransfer.setMessage(response.getBody());
                return clienteTransfer;
            }

            clienteTransfer.setAction("failed-cliente");
            clienteTransfer.setMessage(Objects.requireNonNull(response.getBody()));
            return clienteTransfer;
        }

        if (clienteTransfer.getAction().equals("delete-cliente")) {
            UUID idDelete = UUID.fromString(clienteTransfer.getMessage());

            ResponseEntity<ClienteDto> response = clienteHelper.deleteCliente(idDelete);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                clienteTransfer.setAction("success-cliente");
                clienteTransfer.setClienteDto(response.getBody());
                clienteTransfer.setMessage("Cliente deletado com sucesso.");
                return clienteTransfer;
            }

            clienteTransfer.setAction("failed-cliente");
            clienteTransfer.setMessage("Cliente não encontrado.");
            return clienteTransfer;
        }

        clienteTransfer.setAction("failed-cliente");
        clienteTransfer.setMessage("Ação informada não existe.");
        return clienteTransfer;
    }
}
