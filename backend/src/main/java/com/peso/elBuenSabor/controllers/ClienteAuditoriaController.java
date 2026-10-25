package com.peso.elBuenSabor.controllers;

import com.peso.elBuenSabor.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("elbuensabor/v1/clientes/auditoria")
public class ClienteAuditoriaController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/todos")
    public ResponseEntity<?> verTodos() {
        return ResponseEntity.ok(clienteService.findAllIncludingDeleted());
    }

    @GetMapping("/eliminados")
    public ResponseEntity<?> verEliminados() {
        return ResponseEntity.ok(clienteService.findDeleted());
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clienteService.restore(id));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"Error\":\"No se pudo restaurar el cliente\"}");
        }
    }
}
