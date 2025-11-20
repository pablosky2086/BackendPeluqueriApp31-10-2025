package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    List<Cliente> findByNombreCompletoContaining(String nombre);

    // PARA REVISAR
    @Query(value = "SELECT * FROM clientes WHERE alergenos LIKE %:alergenos%", nativeQuery = true)
    List<Cliente> findClientesByAlergenosParcial(@Param("alergenos") String alergenos);

    // PARA REVISAR
    @Query(value = "SELECT * FROM clientes WHERE observaciones LIKE %:observaciones%", nativeQuery = true)
    List<Cliente> findClientesByObservacionesParcial(@Param("observaciones") String observaciones);



}
