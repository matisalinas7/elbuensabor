package com.peso.elBuenSabor.repositories;

import com.peso.elBuenSabor.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingOrApellidoContaining(String nombre, String apellido);
    Page<Cliente> findByNombreContainingOrApellidoContaining(String nombre, String apellido , Pageable pageable);

    //Anotacion JPQL parametros indexados

    @Query(value = "SELECT p FROM Cliente p WHERE p.nombre LIKE %:filtro% OR p.apellido LIKE %:filtro%")
    List<Cliente> search(@Param("filtro") String filtro);
    @Query(value = "SELECT p FROM Cliente p WHERE p.nombre LIKE %:filtro% OR p.apellido LIKE %:filtro%")
    Page<Cliente> search(@Param("filtro") String filtro , Pageable pageable);

    // Las queries nativas no aplican el @Where de Cliente: hay que excluir los eliminados a mano
    @Query(value = "SELECT * FROM Cliente WHERE (Cliente.nombre LIKE %:filtro% OR Cliente.apellido LIKE %:filtro%) AND Cliente.fecha_baja IS NULL",
            nativeQuery = true)
    List<Cliente> searchNativo(@Param("filtro") String filtro);

    @Query(value = "SELECT * FROM Cliente WHERE (Cliente.nombre LIKE %:filtro% OR Cliente.apellido LIKE %:filtro%) AND Cliente.fecha_baja IS NULL",
            countQuery = "SELECT count(*) FROM Cliente WHERE (Cliente.nombre LIKE %:filtro% OR Cliente.apellido LIKE %:filtro%) AND Cliente.fecha_baja IS NULL",
            nativeQuery = true)
    Page<Cliente> searchNativo(@Param("filtro") String filtro, Pageable pageable);

    // Incluye activos + eliminados
    @Query(value = "SELECT * FROM cliente", nativeQuery = true)
    List<Cliente> findAllIncludingDeleted();

    // Solo eliminados
    @Query(value = "SELECT * FROM cliente WHERE fecha_baja IS NOT NULL", nativeQuery = true)
    List<Cliente> findDeleted();

    @Query(value = "SELECT * FROM cliente WHERE id = :id", nativeQuery = true)
    Optional<Cliente> findByIdIncludingDeleted(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE cliente SET fecha_baja = NULL WHERE id = :id", nativeQuery = true)
    void restoreById(Long id);

}
