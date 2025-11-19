package com.example.demo.service;

import com.example.demo.model.Grupo;
import com.example.demo.model.Usuario;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrupoService extends UsuarioService{
    private final GrupoRepository grupoRepository;

    public GrupoService(UsuarioRepository usuarioRepository, GrupoRepository grupoRepository) {
        super(usuarioRepository);
        this.grupoRepository = grupoRepository;
    }

    public Optional<Grupo> update (Long id, Grupo grupo){
        Optional<Usuario> oldGrupo = grupoRepository.findById(id);
        if (oldGrupo.isEmpty()){
            return Optional.empty();
        }
        Grupo newGrupo = (Grupo) oldGrupo.get();
        if (grupo.getNombre_completo()!=null) newGrupo.setNombre_completo(grupo.getNombre_completo());
        if (grupo.getEmail()!=null) newGrupo.setEmail(grupo.getEmail());
        if (grupo.getContrasena()!=null) newGrupo.setContrasena(grupo.getContrasena());
        if (grupo.getTurno()!=null) newGrupo.setTurno(grupo.getTurno());
        if (grupo.getClase()!=null) newGrupo.setClase(grupo.getClase());

        grupoRepository.save(newGrupo);

        return Optional.of(newGrupo);
    }

    public Usuario save (Grupo grupo){return grupoRepository.save(grupo);}
}
