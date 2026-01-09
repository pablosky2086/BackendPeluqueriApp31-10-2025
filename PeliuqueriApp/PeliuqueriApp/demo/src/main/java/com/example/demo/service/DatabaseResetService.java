package com.example.demo.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DatabaseResetService {

    private final EntityManager entityManager;

    @Transactional
    public void resetDatabase() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE agendas").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE servicios").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE grupos").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE tipos_servicio").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE clientes").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE admins").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE usuarios").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE grupo_servicio").executeUpdate();


        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}

