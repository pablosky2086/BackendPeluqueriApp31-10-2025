package com.example.demo.repository;

import com.example.demo.model.Admin;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query(value = "SELECT * FROM admins WHERE especialidad LIKE %:especialidad%", nativeQuery = true)
    List<Admin> findAdminsByEspecialidadParcial(@Param("especialidad") String especialidad);
}
