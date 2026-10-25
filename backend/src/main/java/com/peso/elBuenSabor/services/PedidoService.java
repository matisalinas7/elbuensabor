package com.peso.elBuenSabor.services;

import com.peso.elBuenSabor.DTOs.DTOnuevopedido;
import com.peso.elBuenSabor.entities.Cliente;
import com.peso.elBuenSabor.entities.Pedido;
import com.peso.elBuenSabor.enums.EstadoPedido;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {

    List<Pedido> findPedidosByClienteId(Long cliente_id);

    Pedido crearPedido(Cliente cliente, DTOnuevopedido nuevoPedido);

    Pedido cambiarEstado(Long id, EstadoPedido estado);

}
