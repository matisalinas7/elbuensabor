package com.peso.elBuenSabor.DTOs;

import com.peso.elBuenSabor.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Perfil del usuario logueado; cliente es null para empleados/admins sin ficha de cliente
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTOme {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private Role role;
    private DTOCliente cliente;
}
