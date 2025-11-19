package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends UsuarioRepository{
    @Query(value = "SELECT * FROM clientes WHERE alergenos LIKE %:alergenos%", nativeQuery = true)
    List<Usuario> findClientesByAlergenosParcial(@Param("alergenos") String alergenos);

    @Query(value = "SELECT * FROM clientes WHERE observaciones LIKE %:observaciones%", nativeQuery = true)
    List<Usuario> findClientesByObservacionesParcial(@Param("observaciones") String observaciones);
}
