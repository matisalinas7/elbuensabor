package com.peso.elBuenSabor.services;

import com.peso.elBuenSabor.DTOs.DTOnuevopedido;
import com.peso.elBuenSabor.entities.*;
import com.peso.elBuenSabor.enums.EstadoPedido;
import com.peso.elBuenSabor.enums.TipoEnvio;
import com.peso.elBuenSabor.repositories.ArticuloManufacturadoRepository;
import com.peso.elBuenSabor.repositories.BaseRepository;
import com.peso.elBuenSabor.repositories.DomicilioRepository;
import com.peso.elBuenSabor.repositories.FacturaRepository;
import com.peso.elBuenSabor.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PedidoServiceImpl extends BaseServiceImpl<Pedido, Long> implements PedidoService {

    // Margen que se suma al tiempo de cocina cuando el pedido sale por delivery
    private static final int MINUTOS_DELIVERY = 20;

    private final PedidoRepository pedidoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    public PedidoServiceImpl(BaseRepository<Pedido, Long> baseRepository, PedidoRepository pedidoRepository) {
        super(baseRepository);
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> findPedidosByClienteId(Long clienteId){
        return pedidoRepository.findByClienteIdOrderByFechaPedidoDesc(clienteId);
    }

    @Override
    @Transactional
    public Pedido crearPedido(Cliente cliente, DTOnuevopedido nuevoPedido) {
        if (nuevoPedido.getItems() == null || nuevoPedido.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido no tiene productos");
        }
        if (nuevoPedido.getTipoEnvio() == null || nuevoPedido.getFormaPago() == null) {
            throw new IllegalArgumentException("Falta el tipo de envío o la forma de pago");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setTipoEnvio(nuevoPedido.getTipoEnvio());
        pedido.setFormaPago(nuevoPedido.getFormaPago());
        pedido.setEstado(EstadoPedido.PENDIENTE_PAGO);
        pedido.setFechaPedido(new Date());

        if (nuevoPedido.getTipoEnvio() == TipoEnvio.DELIVERY) {
            pedido.setDomicilio(resolverDomicilio(cliente, nuevoPedido));
        }

        BigDecimal totalCosto = BigDecimal.ZERO;
        int minutosCocina = 0;

        for (DTOnuevopedido.Item item : nuevoPedido.getItems()) {
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("Cantidad inválida");
            }
            ArticuloManufacturado articulo = articuloManufacturadoRepository.findById(item.getArticuloId())
                    .orElseThrow(() -> new EntityNotFoundException("No existe el artículo " + item.getArticuloId()));

            BigDecimal costoUnitario = articulo.getCosto() != null ? articulo.getCosto() : BigDecimal.ZERO;

            DetallePedido detalle = new DetallePedido();
            detalle.setArticuloManufacturado(articulo);
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotalCosto(costoUnitario.multiply(BigDecimal.valueOf(item.getCantidad())));
            detalle.setPedido(pedido);
            pedido.agregarDetalle(detalle);

            totalCosto = totalCosto.add(detalle.getSubtotalCosto());
            // Los productos se cocinan en paralelo: manda el más lento
            minutosCocina = Math.max(minutosCocina, articulo.getTiempoEstimadoCocina());
        }

        if (nuevoPedido.getTipoEnvio() == TipoEnvio.DELIVERY) {
            minutosCocina += MINUTOS_DELIVERY;
        }

        pedido.setTotalCosto(totalCosto);
        pedido.setHoraEstimadaFinalizacion(new Date(System.currentTimeMillis() + minutosCocina * 60_000L));

        return pedidoRepository.save(pedido);
    }

    private Domicilio resolverDomicilio(Cliente cliente, DTOnuevopedido nuevoPedido) {
        if (nuevoPedido.getDomicilioId() != null) {
            Domicilio domicilio = domicilioRepository.findById(nuevoPedido.getDomicilioId())
                    .orElseThrow(() -> new EntityNotFoundException("No existe el domicilio"));
            if (domicilio.getCliente() == null || !domicilio.getCliente().getId().equals(cliente.getId())) {
                throw new IllegalArgumentException("El domicilio no pertenece al cliente");
            }
            return domicilio;
        }

        Domicilio domicilio = nuevoPedido.getDomicilio();
        if (domicilio == null || domicilio.getCalle() == null || domicilio.getNumero() == null
                || domicilio.getCodigoPostal() == null || domicilio.getLocalidad() == null) {
            throw new IllegalArgumentException("Para delivery hace falta un domicilio completo");
        }
        domicilio.setId(null);
        domicilio.setCliente(cliente);
        return domicilioRepository.save(domicilio);
    }

    @Override
    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el pedido con id " + id));
        pedido.setEstado(estado);

        // Un pedido cobrado se factura una sola vez (efectivo puede pasar directo a COMPLETADO)
        boolean cobrado = estado == EstadoPedido.PAGADO || estado == EstadoPedido.COMPLETADO;
        if (cobrado && !facturaRepository.existsByPedidoId(id)) {
            Factura factura = new Factura();
            factura.setPedido(pedido);
            factura.setFechaFacturacion(new Date());
            factura.setFormaPago(pedido.getFormaPago());
            factura.setTotalVenta(pedido.getTotal().doubleValue());
            facturaRepository.save(factura);
        }

        return pedidoRepository.save(pedido);
    }

}
