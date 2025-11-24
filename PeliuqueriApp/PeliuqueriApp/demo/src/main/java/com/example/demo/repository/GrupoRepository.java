package com.example.demo.repository;

import com.example.demo.model.Grupo;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    @Query(value = "SELECT * FROM grupos WHERE turno LIKE %:turno%", nativeQuery = true)
    List<Grupo> findGruposByTurnoParcial(@Param("turno") String turno);
}
