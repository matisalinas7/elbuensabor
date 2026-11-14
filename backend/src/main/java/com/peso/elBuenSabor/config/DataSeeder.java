package com.peso.elBuenSabor.config;

import com.peso.elBuenSabor.entities.*;
import com.peso.elBuenSabor.enums.Role;
import com.peso.elBuenSabor.repositories.ArticuloInsumoRepository;
import com.peso.elBuenSabor.repositories.ArticuloManufacturadoRepository;
import com.peso.elBuenSabor.repositories.RubroArticuloRepository;
import com.peso.elBuenSabor.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Datos iniciales: el registro público solo crea clientes, así que sin esto no hay forma de tener un admin.
// Solo actúa sobre una base vacía; no pisa nada existente.
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RubroArticuloRepository rubroArticuloRepository;
    private final ArticuloInsumoRepository articuloInsumoRepository;
    private final ArticuloManufacturadoRepository articuloManufacturadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        boolean hayAdmin = userRepository.findAll().stream().anyMatch(u -> u.getRole() == Role.ADMIN);
        if (!hayAdmin) {
            crearUsuario("admin", "admin123", "Admin", "El Buen Sabor", Role.ADMIN);
            if (userRepository.findByUsername("empleado").isEmpty()) {
                crearUsuario("empleado", "empleado123", "Empleado", "El Buen Sabor", Role.EMPLOYEE);
            }
        }

        if (rubroArticuloRepository.count() == 0) {
            crearCatalogoDemo();
        }
    }

    private void crearUsuario(String username, String password, String firstname, String lastname, Role role) {
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .firstname(firstname)
                .lastname(lastname)
                .country("Argentina")
                .role(role)
                .build());
    }

    private void crearCatalogoDemo() {
        RubroArticulo hamburguesas = rubro("Hamburguesas");
        RubroArticulo pizzas = rubro("Pizzas");
        RubroArticulo acompanamientos = rubro("Acompañamientos");
        RubroArticulo bebidas = rubro("Bebidas");

        ArticuloInsumo pan = insumo("Pan de hamburguesa", "Pan brioche", "250", "120", "30", "unidad");
        ArticuloInsumo carne = insumo("Medallón de carne", "Medallón de 120 g", "900", "80", "30", "unidad");
        ArticuloInsumo cheddar = insumo("Queso cheddar", "Fetas de cheddar", "150", "200", "50", "feta");
        ArticuloInsumo masa = insumo("Prepizza", "Prepizza de 8 porciones", "700", "40", "15", "unidad");
        ArticuloInsumo muzzarella = insumo("Muzzarella", "Muzzarella en barra", "6000", "10", "4", "kg");
        ArticuloInsumo tomate = insumo("Tomate", "Tomate redondo", "1500", "3", "5", "kg");
        ArticuloInsumo papas = insumo("Papas", "Papas para freír", "800", "25", "10", "kg");
        ArticuloInsumo gaseosa = insumo("Gaseosa 500 ml", "Botella de 500 ml", "900", "48", "24", "unidad");

        producto("Hamburguesa clásica", "Medallón de carne, cheddar y pan brioche.", 15, "5500", "1300", hamburguesas,
                receta(pan, "1", "unidad"), receta(carne, "1", "unidad"), receta(cheddar, "1", "feta"));
        producto("Hamburguesa doble cheddar", "Doble medallón con doble cheddar.", 18, "7800", "2350", hamburguesas,
                receta(pan, "1", "unidad"), receta(carne, "2", "unidad"), receta(cheddar, "2", "feta"));
        producto("Pizza muzzarella", "Clásica de muzzarella, 8 porciones.", 20, "8500", "3100", pizzas,
                receta(masa, "1", "unidad"), receta(muzzarella, "0.4", "kg"));
        producto("Pizza napolitana", "Muzzarella con rodajas de tomate fresco.", 22, "9500", "3550", pizzas,
                receta(masa, "1", "unidad"), receta(muzzarella, "0.4", "kg"), receta(tomate, "0.3", "kg"));
        producto("Papas fritas", "Porción grande de papas fritas.", 10, "3500", "400", acompanamientos,
                receta(papas, "0.5", "kg"));
        producto("Gaseosa 500 ml", "Línea Coca-Cola, bien fría.", 1, "2000", "900", bebidas,
                receta(gaseosa, "1", "unidad"));
    }

    private RubroArticulo rubro(String denominacion) {
        RubroArticulo rubro = new RubroArticulo();
        rubro.setDenominacion(denominacion);
        return rubroArticuloRepository.save(rubro);
    }

    private ArticuloInsumo insumo(String nombre, String descripcion, String precioCompra, String stockActual,
                                  String stockMinimo, String unidadMedida) {
        ArticuloInsumo insumo = new ArticuloInsumo();
        insumo.setNombre(nombre);
        insumo.setDescripcion(descripcion);
        insumo.setPrecioCompra(new BigDecimal(precioCompra));
        insumo.setStockActual(new BigDecimal(stockActual));
        insumo.setStockMinimo(new BigDecimal(stockMinimo));
        insumo.setUnidadMedida(unidadMedida);
        return articuloInsumoRepository.save(insumo);
    }

    private DetalleArticuloManufacturado receta(ArticuloInsumo insumo, String cantidad, String unidadMedida) {
        DetalleArticuloManufacturado detalle = new DetalleArticuloManufacturado();
        detalle.setArticuloInsumo(insumo);
        detalle.setCantidad(new BigDecimal(cantidad));
        detalle.setUnidadMedida(unidadMedida);
        return detalle;
    }

    private void producto(String denominacion, String descripcion, int minutos, String precioVenta, String costo,
                          RubroArticulo rubro, DetalleArticuloManufacturado... receta) {
        ArticuloManufacturado producto = new ArticuloManufacturado();
        producto.setDenominacion(denominacion);
        producto.setDescripcion(descripcion);
        producto.setTiempoEstimadoCocina(minutos);
        producto.setPrecioVenta(new BigDecimal(precioVenta));
        producto.setCosto(new BigDecimal(costo));
        producto.setRubroArticulo(rubro);
        producto.getDetalleArticulosManufacturados().addAll(List.of(receta));
        articuloManufacturadoRepository.save(producto);
    }
}
