package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends UsuarioRepository{
    @Query(value = "SELECT * FROM admins WHERE especialidad LIKE %:especialidad%", nativeQuery = true)
    List<Usuario> findAdminsByEspecialidadParcial(@Param("especialidad") String especialidad);
}
