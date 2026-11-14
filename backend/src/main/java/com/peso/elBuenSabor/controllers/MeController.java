package com.peso.elBuenSabor.controllers;

import com.peso.elBuenSabor.DTOs.DTOme;
import com.peso.elBuenSabor.DTOs.DTOnuevopedido;
import com.peso.elBuenSabor.entities.Cliente;
import com.peso.elBuenSabor.entities.User;
import com.peso.elBuenSabor.mapper.ClienteMapper;
import com.peso.elBuenSabor.repositories.DomicilioRepository;
import com.peso.elBuenSabor.services.ClienteService;
import com.peso.elBuenSabor.services.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Endpoints del usuario logueado: el cliente sale del token, nunca de un id en la URL
@RestController
@RequestMapping("elbuensabor/v1/me")
public class MeController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @GetMapping("")
    public DTOme me(@AuthenticationPrincipal User user) {
        Cliente cliente = clienteService.findByUser(user).orElse(null);
        return new DTOme(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(),
                user.getRole(), ClienteMapper.toDTO(cliente));
    }

    @GetMapping("/pedidos")
    public ResponseEntity<?> misPedidos(@AuthenticationPrincipal User user) {
        return clienteService.findByUser(user)
                .<ResponseEntity<?>>map(c -> ResponseEntity.ok(pedidoService.findPedidosByClienteId(c.getId())))
                .orElseGet(() -> ResponseEntity.ok(List.of()));
    }

    @PostMapping("/pedidos")
    public ResponseEntity<?> nuevoPedido(@AuthenticationPrincipal User user, @RequestBody DTOnuevopedido body) {
        try {
            Cliente cliente = clienteService.findOrCreateForUser(user);
            return ResponseEntity.ok(pedidoService.crearPedido(cliente, body));
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/domicilios")
    public ResponseEntity<?> misDomicilios(@AuthenticationPrincipal User user) {
        return clienteService.findByUser(user)
                .<ResponseEntity<?>>map(c -> ResponseEntity.ok(domicilioRepository.findByClienteId(c.getId())))
                .orElseGet(() -> ResponseEntity.ok(List.of()));
    }
}
