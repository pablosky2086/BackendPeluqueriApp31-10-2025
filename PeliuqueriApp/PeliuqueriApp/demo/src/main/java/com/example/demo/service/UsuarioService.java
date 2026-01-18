package com.example.demo.service;

import com.example.demo.model.Servicio;
import com.example.demo.model.Usuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll(){return usuarioRepository.findAll();}

    public Optional<Usuario> findById(Long id) {return usuarioRepository.findById(id);}

    public void delete(Usuario usuario){usuarioRepository.delete(usuario);}

    public void deleteById(Long id){usuarioRepository.deleteById(id);}

    public List<Usuario> findUsuariosByNombreParcial(String nombre){
        return usuarioRepository.findUsuariosByNombreParcial(nombre);
    }

    public Optional<Usuario> findByEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        // Comprobar si la contraseña antigua hace match con la almacenada
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(oldPassword, usuario.getContrasena())) {
                // Actualizar a la nueva contraseña
                usuario.setContrasena(passwordEncoder.encode(newPassword));
                usuarioRepository.save(usuario);
                return true;
            } else {
                return false; // La contraseña antigua no coincide
            }
        } else {
            return false; // Usuario no encontrado
        }
    }
}
