package com.example.demo.service;

import com.example.demo.model.Grupo;
import com.example.demo.model.Turno;
import com.example.demo.model.Usuario;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService{
    private final GrupoRepository grupoRepository;

    public GrupoService(UsuarioRepository usuarioRepository, GrupoRepository grupoRepository) {this.grupoRepository = grupoRepository;}

    public List<Grupo> findAll() {return grupoRepository.findAll();}

    public Optional<Grupo> findById(Long id) { return grupoRepository.findById(id); }

    public List<Grupo> findByTurnoParcial(String turno) {
        System.out.println("Buscando grupos con turno que contiene: " + turno);
        Turno t = Turno.valueOf(turno.toUpperCase());
        System.out.println("Turno convertido: " + t);
        return grupoRepository.findByTurno(t);
    }

    public Optional<Grupo> update (Long id, Grupo grupo){
        Optional<Grupo> oldGrupo = grupoRepository.findById(id);
        if (oldGrupo.isEmpty()){
            return Optional.empty();
        }
        Grupo newGrupo = (Grupo) oldGrupo.get();
        if (grupo.getNombreCompleto()!=null) newGrupo.setNombreCompleto(grupo.getNombreCompleto());
        if (grupo.getEmail()!=null) newGrupo.setEmail(grupo.getEmail());
        if (grupo.getContrasena()!=null) newGrupo.setContrasena(grupo.getContrasena());
        if (grupo.getTurno()!=null) newGrupo.setTurno(grupo.getTurno());
        if (grupo.getClase()!=null) newGrupo.setClase(grupo.getClase());

        grupoRepository.save(newGrupo);

        return Optional.of(newGrupo);
    }

    public Usuario save (Grupo grupo){return grupoRepository.save(grupo);}

    public void deleteById(Long id){grupoRepository.deleteById(id);}
}
