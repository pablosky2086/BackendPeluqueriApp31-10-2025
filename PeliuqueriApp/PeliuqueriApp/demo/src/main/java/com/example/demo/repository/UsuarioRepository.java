package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query(value = "SELECT * FROM usuarios WHERE nombre LIKE %:nombre%", nativeQuery = true)
    List<Usuario> findUsuariosByNombreParcial(@Param("nombre") String nombre);

    Boolean existsByEmail(String email);
}
