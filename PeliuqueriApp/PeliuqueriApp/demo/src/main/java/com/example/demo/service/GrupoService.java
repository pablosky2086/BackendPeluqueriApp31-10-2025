package com.example.demo.service;

import com.example.demo.model.Grupo;
import com.example.demo.model.Servicio;
import com.example.demo.model.Usuario;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService{
    private final GrupoRepository grupoRepository;
    private final ServicioRepository servicioRepository;

    public GrupoService(UsuarioRepository usuarioRepository, GrupoRepository grupoRepository, ServicioRepository servicioRepository) {this.grupoRepository = grupoRepository;
        this.servicioRepository = servicioRepository;
    }

    public List<Grupo> findAll() {return grupoRepository.findAll();}

    public Optional<Grupo> findById(Long id) { return grupoRepository.findById(id); }

    /*
    public List<Grupo> findByTurno(String turno) {
        System.out.println("Buscando grupos con turno que contiene: " + turno);
        Turno t = Turno.valueOf(turno.toUpperCase());
        System.out.println("Turno convertido: " + t);
        return grupoRepository.findByTurno(t);
    }
     */

    public Optional<Grupo> update (Long id, Grupo grupo){
        Optional<Grupo> oldGrupo = grupoRepository.findById(id);
        if (oldGrupo.isEmpty()){
            return Optional.empty();
        }
        Grupo newGrupo = (Grupo) oldGrupo.get();
        if (grupo.getNombreCompleto()!=null) newGrupo.setNombreCompleto(grupo.getNombreCompleto());
        if (grupo.getEmail()!=null) newGrupo.setEmail(grupo.getEmail());
        if (grupo.getContrasena()!=null) newGrupo.setContrasena(grupo.getContrasena());
        if (grupo.getClase()!=null) newGrupo.setClase(grupo.getClase());
        if (grupo.getModulo()!=null) newGrupo.setModulo(grupo.getModulo());
        if (grupo.getAula()!=null) newGrupo.setAula(grupo.getAula());

        grupoRepository.save(newGrupo);

        return Optional.of(newGrupo);
    }

    public Usuario save (Grupo grupo){return grupoRepository.save(grupo);}

    public void deleteById(Long id){grupoRepository.deleteById(id);}

    public boolean addServicioToGrupo(Long grupoId, Long servicioId) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        Optional<Servicio> servicioOpt = servicioRepository.findById(servicioId);

        if (grupoOpt.isEmpty() || servicioOpt.isEmpty()) {
            return false; // Grupo or Servicio not found
        }

        Grupo grupo = grupoOpt.get();
        Servicio servicio = servicioOpt.get();

        grupo.addServicio(servicio);
        grupoRepository.save(grupo);

        return true;
    }

    public boolean removeServicioFromGrupo(Long grupoId, Long servicioId) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        Optional<Servicio> servicioOpt = servicioRepository.findById(servicioId);

        if (grupoOpt.isEmpty() || servicioOpt.isEmpty()) {
            return false; // Grupo or Servicio not found
        }

        Grupo grupo = grupoOpt.get();
        Servicio servicio = servicioOpt.get();

        grupo.getServicios().remove(servicio);
        grupoRepository.save(grupo);

        return true;
    }
}
