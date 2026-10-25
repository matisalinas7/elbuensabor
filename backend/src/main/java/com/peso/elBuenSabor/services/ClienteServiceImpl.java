package com.peso.elBuenSabor.services;

import com.peso.elBuenSabor.entities.Cliente;
import com.peso.elBuenSabor.repositories.BaseRepository;
import com.peso.elBuenSabor.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, Long> implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteServiceImpl(BaseRepository<Cliente, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public List<Cliente> search(String filtro) throws Exception {
        try {
            return clienteRepository.searchNativo(filtro);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Page<Cliente> search(String filtro, Pageable pageable) throws Exception {
        try {
            Page<Cliente> clientes = clienteRepository.searchNativo(filtro, pageable);
            return clientes;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Cliente> findAllIncludingDeleted() {
        return clienteRepository.findAllIncludingDeleted();
    }

    @Override
    public List<Cliente> findDeleted() {
        return clienteRepository.findDeleted();
    }

    @Transactional
    @Override
    public Cliente restore(Long id) throws Exception {

        Cliente eliminado = clienteRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new Exception("Cliente no encontrado"));

        clienteRepository.restoreById(id);

        return clienteRepository.findById(id)
                .orElseThrow(() -> new Exception("Error al restaurar el cliente"));
    }

}
