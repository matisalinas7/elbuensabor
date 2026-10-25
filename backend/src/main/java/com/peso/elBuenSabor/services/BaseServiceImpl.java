package com.peso.elBuenSabor.services;

import com.peso.elBuenSabor.entities.Base;
import com.peso.elBuenSabor.repositories.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<E extends Base, ID extends Serializable> implements BaseService<E, ID> {

    protected BaseRepository<E,ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    @Transactional
    public List<E> findAll() throws Exception {
        try {
            List<E> entities = baseRepository.findAll();
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Page<E> findAll(Pageable pageable) throws Exception {
        try {
            Page<E> entities = baseRepository.findAll(pageable);
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }    }

    @Override
    @Transactional
    public E findById(ID id) throws Exception {
        try {
            return baseRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException("No se encontró la entidad con id " + id));
        } catch (EntityNotFoundException e) {
            // Dejar que la excepción suba para que el controller devuelva 404
            throw e;
        } catch (Exception e) {
            throw new Exception("Error en findById: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public E save(E entity) throws Exception {
        try{
            entity = baseRepository.save(entity);
            return entity;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public E update(ID id, E entity) throws Exception {
        E entityActual = baseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("No se encontró la entidad con id " + id)
                );

        // El id sale de la URL y los datos de auditoría de la base, no del body
        entity.setId(entityActual.getId());
        entity.setFechaAlta(entityActual.getFechaAlta());
        entity.setFechaBaja(entityActual.getFechaBaja());

        return baseRepository.save(entity);
    }

//    @Override
//    @Transactional
//    public boolean delete(ID id) throws Exception {
//        try{
//            if(baseRepository.existsById(id)){
//                baseRepository.deleteById(id);
//                return true;
//            } else {
//                throw new Exception();
//            }
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }
    @Override
    @Transactional
    public boolean delete(ID id) {
        E entity = baseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("No se encontró la entidad con id " + id)
                );

        baseRepository.delete(entity);
        return true;
    }


}
