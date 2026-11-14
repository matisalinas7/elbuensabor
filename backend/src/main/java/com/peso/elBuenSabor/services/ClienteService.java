package com.peso.elBuenSabor.services;

import com.peso.elBuenSabor.entities.Cliente;
import com.peso.elBuenSabor.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClienteService extends BaseService<Cliente, Long> {
    List<Cliente> search(String filtro) throws Exception;
    Page<Cliente> search(String filtro , Pageable pageable) throws Exception;
    List<Cliente> findAllIncludingDeleted();
    List<Cliente> findDeleted();
    Cliente restore(Long id) throws Exception;
    Optional<Cliente> findByUser(User user);
    Cliente findOrCreateForUser(User user);
}
