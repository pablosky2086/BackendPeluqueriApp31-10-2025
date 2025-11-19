package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrupoRepository extends UsuarioRepository{
    @Query(value = "SELECT * FROM grupos WHERE turno LIKE %:turno%", nativeQuery = true)
    List<Usuario> findGruposByTurnoParcial(@Param("turno") String turno);
}
