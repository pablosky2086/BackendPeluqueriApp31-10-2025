package com.example.demo.repository;

import com.example.demo.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda,Long> {

    // Obtener agendas por grupo
    List<Agenda> findByGrupoId(Long grupoId);

    // Obtener agendas por servicio
    List<Agenda> findByServicioId(Long servicioId);

    // Obtener agendas por grupo y servicio
    List<Agenda> findByGrupoIdAndServicioId(Long grupoId, Long servicioId);
}
