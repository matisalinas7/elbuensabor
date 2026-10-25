package com.peso.elBuenSabor.controllers;

import com.peso.elBuenSabor.DTOs.DTOcambiarestado;
import com.peso.elBuenSabor.entities.Pedido;
import com.peso.elBuenSabor.services.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "elbuensabor/v1/pedidos")
public class PedidoController extends BaseControllerImpl<Pedido, PedidoService> {

    @GetMapping("/cliente/{clienteId}")
        public List<Pedido> getPedidosByClienteId(@PathVariable Long clienteId) {
            return servicio.findPedidosByClienteId(clienteId);
        }

    // Cambia solo el estado, sin tener que mandar el pedido completo por PUT
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody DTOcambiarestado body) {
        if (body.getEstadoPedido() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Error\":\"Falta el estado\"}");
        }
        try {
            return ResponseEntity.ok(servicio.cambiarEstado(id, body.getEstadoPedido()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"Error\":\"" + e.getMessage() + "\"}");
        }
    }

}
