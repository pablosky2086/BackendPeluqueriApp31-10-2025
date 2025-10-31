package com.example.demo.service;

import com.example.demo.model.Servicio;
import com.example.demo.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> findAll(){return servicioRepository.findAll();}

    public Optional<Servicio> findById(Long id) {return servicioRepository.findById(id);}

    public Servicio save (Servicio servicio){return servicioRepository.save(servicio);}

    public void delete(Servicio servicio){servicioRepository.delete(servicio);}

    public void deleteById(Long id){servicioRepository.deleteById(id);}

    public Optional<Servicio> update (Long id, Servicio servicio){
        Optional<Servicio> oldServicio = servicioRepository.findById(id);
        if (oldServicio.isEmpty()){
            return Optional.empty();
        }
        Servicio newServicio = oldServicio.get();
        if (servicio.getNombre()!=null) newServicio.setNombre(servicio.getNombre());
        if (servicio.getDescripcion()!=null) newServicio.setDescripcion(servicio.getDescripcion());
        if (servicio.getDuracion()!=null) newServicio.setDuracion(servicio.getDuracion());
        if (servicio.getPrecio()!=null) newServicio.setPrecio(servicio.getPrecio());

        servicioRepository.save(newServicio);

        return Optional.of(newServicio);
    }

    public List<Servicio> findServiciosByNombreParcial(String nombre){
        return servicioRepository.findServicioByNombreParcial(nombre);
    }
}
