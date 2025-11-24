package com.example.demo.repository;

import com.example.demo.model.Admin;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    //Hibernate: select a1_0.id,a1_1.contrasena,a1_1.email,a1_1.nombre_completo,a1_1.role,a1_0.especialidad from admins a1_0 join usuarios a1_1 on a1_0.id=a1_1.id where a1_0.especialidad like ? escape '\\'
    List<Admin> findByEspecialidadContaining(@Param("especialidad") String especialidad);
}
