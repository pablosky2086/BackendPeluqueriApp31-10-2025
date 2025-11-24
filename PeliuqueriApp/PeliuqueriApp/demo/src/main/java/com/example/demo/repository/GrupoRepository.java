package com.example.demo.repository;

import com.example.demo.model.Grupo;
import com.example.demo.model.Turno;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByTurno(Turno turno);
}
