package com.example.demo.service;

import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private UserDetails getUserDetails() {
        return (UserDetails) getAuth().getPrincipal();
    }

    public String getCurrentUserEmail() {
        return getUserDetails().getUsername();
    }

    public String getCurrentUserRole() {
        return getUserDetails().getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse(null);
    }

    public Long getCurrentUserId() {
        return usuarioRepository.findByEmail(getCurrentUserEmail())
                .map(u -> u.getId())
                .orElse(null);
    }

    public boolean isAdmin() { return "ROLE_ADMIN".equals(getCurrentUserRole()); }

    public boolean isGrupo() { return "ROLE_GRUPO".equals(getCurrentUserRole()); }

    public boolean isCliente() { return "ROLE_CLIENTE".equals(getCurrentUserRole()); }

    public boolean isOwnerOfCliente(Long clienteId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return usuarioRepository.findById(clienteId)
                .map(clienteUsuario -> clienteUsuario.getId().equals(currentUserId))
                .orElse(false);
    }
}
