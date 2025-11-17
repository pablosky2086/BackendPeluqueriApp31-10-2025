package com.example.demo.service;

import com.example.demo.model.Servicio;
import com.example.demo.model.Usuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {this.usuarioRepository = usuarioRepository;}

    public List<Usuario> findAll(){return usuarioRepository.findAll();}

    public Optional<Usuario> findById(Long id) {return usuarioRepository.findById(id);}

    public void delete(Usuario usuario){usuarioRepository.delete(usuario);}

    public void deleteById(Long id){usuarioRepository.deleteById(id);}

    public List<Usuario> findUsuariosByNombreParcial(String nombre){
        return usuarioRepository.findUsuariosByNombreParcial(nombre);
    }
}
