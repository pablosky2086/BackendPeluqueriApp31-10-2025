package com.example.demo.repository;

import com.example.demo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Todas las citas de un cliente
    List<Cita> findByClienteId(Long clienteId);

    // Todas las citas de un grupo
    List<Cita> findByGrupoId(Long grupoId);

    // Todas las citas de un servicio
    List<Cita> findByServicioId(Long servicioId);
}
