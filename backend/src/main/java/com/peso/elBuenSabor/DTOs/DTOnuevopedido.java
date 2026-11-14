package com.peso.elBuenSabor.DTOs;

import com.peso.elBuenSabor.entities.Domicilio;
import com.peso.elBuenSabor.enums.FormaPago;
import com.peso.elBuenSabor.enums.TipoEnvio;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// Pedido armado desde el carrito: el backend calcula totales, horario y estado
@Data
public class DTOnuevopedido {
    private TipoEnvio tipoEnvio;
    private FormaPago formaPago;
    // Para DELIVERY: un domicilio ya guardado del cliente, o uno nuevo
    private Long domicilioId;
    private Domicilio domicilio;
    private List<Item> items = new ArrayList<>();

    @Data
    public static class Item {
        private Long articuloId;
        private Long cantidad;
    }
}
