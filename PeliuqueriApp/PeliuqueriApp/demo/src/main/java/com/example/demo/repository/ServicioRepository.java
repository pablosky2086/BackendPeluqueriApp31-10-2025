package com.example.demo.repository;

import com.example.demo.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    @Query(value = "SELECT * FROM servicios WHERE nombre LIKE %:nombre%", nativeQuery = true)
    List<Servicio> findServicioByNombreParcial(@Param("nombre") String nombre);
}
