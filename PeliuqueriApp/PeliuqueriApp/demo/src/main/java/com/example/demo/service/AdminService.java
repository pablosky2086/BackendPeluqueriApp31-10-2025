package com.example.demo.service;

import com.example.demo.model.Admin;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService extends UsuarioService{
    private final AdminRepository adminRepository;

    public AdminService(UsuarioRepository usuarioRepository, AdminRepository adminRepository) {
        super(usuarioRepository);
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> update (Long id, Admin admin){
        Optional<Usuario> oldAdmin = adminRepository.findById(id);
        if (oldAdmin.isEmpty()){
            return Optional.empty();
        }
        Admin newAdmin = (Admin) oldAdmin.get();
        if (admin.getNombreCompleto()!=null) newAdmin.setNombreCompleto(admin.getNombreCompleto());
        if (admin.getEmail()!=null) newAdmin.setEmail(admin.getEmail());
        if (admin.getContrasena()!=null) newAdmin.setContrasena(admin.getContrasena());
        if (admin.getEspecialidad()!=null) newAdmin.setEspecialidad(admin.getEspecialidad());

        adminRepository.save(newAdmin);

        return Optional.of(newAdmin);
    }

    public Usuario save (Admin admin){return adminRepository.save(admin);}
}
