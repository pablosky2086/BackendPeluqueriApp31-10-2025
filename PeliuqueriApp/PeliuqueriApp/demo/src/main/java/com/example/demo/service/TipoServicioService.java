package com.example.demo.service;

import com.example.demo.model.TipoServicio;
import com.example.demo.repository.TipoServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoServicioService {

    private final TipoServicioRepository tipoServicioRepository;

    public TipoServicioService(TipoServicioRepository tipoServicioRepository) {
        this.tipoServicioRepository = tipoServicioRepository;
    }

    // Obtener todos
    public List<TipoServicio> findAll() {
        return tipoServicioRepository.findAll();
    }

    // Obtener por ID
    public Optional<TipoServicio> findById(Long id) {
        return tipoServicioRepository.findById(id);
    }

    // Crear uno nuevo
    public TipoServicio addTipoServicio(TipoServicio tipoServicio) {
        return tipoServicioRepository.save(tipoServicio);
    }

    // Actualizar
    public Optional<TipoServicio> update(Long id, TipoServicio updated) {
        return tipoServicioRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(updated.getNombre());
                    existing.setUrl_img(updated.getUrl_img());
                    return tipoServicioRepository.save(existing);
                });
    }

    // Eliminar
    public boolean delete(Long id) {
        return tipoServicioRepository.findById(id)
                .map(existing -> {
                    tipoServicioRepository.delete(existing);
                    return true;
                })
                .orElse(false);
    }
}
