package com.peso.elBuenSabor.mapper;

import com.peso.elBuenSabor.DTOs.DTOCliente;
import com.peso.elBuenSabor.entities.Cliente;

public class ClienteMapper {
    public static DTOCliente toDTO(Cliente cliente) {
        if (cliente == null) return null;

        return new DTOCliente(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getTelefono(),
                cliente.getEmail()
        );
    }
}
