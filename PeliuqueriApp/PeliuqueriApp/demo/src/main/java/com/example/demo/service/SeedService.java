package com.example.demo.service;

import com.example.demo.model.Servicio;
import com.example.demo.repository.ServicioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class SeedService {

    private final ServicioRepository servicioRepository;
    private final ObjectMapper objectMapper;

    public SeedService(ServicioRepository servicioRepository, ObjectMapper objectMapper) {
        this.servicioRepository = servicioRepository;
        this.objectMapper = objectMapper;
    }

    public void seedServicios() {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("seed/servicios.json");

            if (inputStream == null) {
                System.err.println("No se encontró seed/data.json");
                return;
            }

            List<Servicio> servicios = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Servicio>>(){}
            );

            if (servicios.isEmpty()) {
                System.out.println("El JSON de servicios está vacío");
                return;
            }

            // Evitar duplicados por nombre
            long countBefore = servicioRepository.count();
            servicioRepository.saveAll(servicios);
            long countAfter = servicioRepository.count();

            System.out.println("Servicios importados: " + (countAfter - countBefore));

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON de servicios", e);
        }
    }
}
