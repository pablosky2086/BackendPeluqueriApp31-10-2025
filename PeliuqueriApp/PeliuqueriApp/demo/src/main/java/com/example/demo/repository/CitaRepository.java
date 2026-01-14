package com.example.demo.repository;

import com.example.demo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    boolean existsByAgendaId(Long agendaId);

    List<Cita> findByClienteId(Long clienteId);

    List<Cita> findByAgendaGrupoId(Long grupoId);

    List<Cita> findByAgendaServicioId(Long servicioId);

    List<Cita> findByAgendaId(Long agendaId);

    List<Cita> findByAgendaHoraInicioBetween(
            LocalDateTime desde,
            LocalDateTime hasta
    );

    List<Cita> findByAgendaGrupoIdAndAgendaHoraInicioBetween(
            Long grupoId,
            LocalDateTime desde,
            LocalDateTime hasta
    );
}
