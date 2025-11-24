package com.example.demo.service;

import com.example.demo.model.Admin;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService{
    private final AdminRepository adminRepository;

    public AdminService(UsuarioRepository usuarioRepository, AdminRepository adminRepository) {this.adminRepository = adminRepository;}

    public List<Admin> findAll() {return adminRepository.findAll();}

    public Optional<Admin> findById(Long id) { return adminRepository.findById(id); }

    public List<Admin> findByPartialEspecialidad(String especialidad) { return adminRepository.findByEspecialidadContaining(especialidad); }

    public Optional<Admin> update (Long id, Admin admin){
        Optional<Admin> oldAdmin = adminRepository.findById(id);
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

    public void deleteById(Long id){adminRepository.deleteById(id);}
}
