package com.example.demo.service;

import com.example.demo.model.Grupo;
import com.example.demo.model.Role;
import com.example.demo.model.Servicio;
import com.example.demo.model.TipoServicio;
import com.example.demo.payload.request.AgendaRequest;
import com.example.demo.repository.AgendaRepository;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.repository.TipoServicioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class SeedService {

    private final ServicioRepository servicioRepository;
    private final ObjectMapper objectMapper;
    private final TipoServicioRepository tipoServicioRepository;
    private final GrupoRepository grupoRepository;
    private final AgendaService agendaService;
    private final PasswordEncoder passwordEncoder;

    public SeedService(ServicioRepository servicioRepository, ObjectMapper objectMapper, TipoServicioRepository tipoServicioRepository, GrupoRepository grupoRepository, AgendaRepository agendaRepository, AgendaService agendaService, PasswordEncoder passwordEncoder) {
        this.servicioRepository = servicioRepository;
        this.objectMapper = objectMapper;
        this.tipoServicioRepository = tipoServicioRepository;
        this.grupoRepository = grupoRepository;
        this.agendaService = agendaService;
        this.passwordEncoder = passwordEncoder;
    }

    public void seedAgenda() {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("seed/agenda.json");
            if (inputStream == null) {
                System.err.println("No se encontró seed/agenda.json");
                return;
            }
            List<AgendaRequest> requests = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<AgendaRequest>>(){}
            );
            if (requests.isEmpty()) {
                System.out.println("El JSON de agenda está vacío");
                return;
            }

            for (AgendaRequest request : requests) {
                agendaService.createAgendasParaUnTiempo(request, 50);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON de agenda", e);
        }
    }

    public void seedTipoServicios() {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("seed/tipos.json");

            if (inputStream == null) {
                System.err.println("No se encontró seed/tipos.json");
                return;
            }

            List<TipoServicio> tipos = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<TipoServicio>>(){}
            );

            if (tipos.isEmpty()) {
                System.out.println("El JSON de tipos de servicio está vacío");
                return;
            }

            // Evitar duplicados por nombre
            long countBefore = tipoServicioRepository.count();
            tipoServicioRepository.saveAll(tipos);
            long countAfter = tipoServicioRepository.count();

            System.out.println("Tipos de servicio importados: " + (countAfter - countBefore));

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON de tipos de servicio", e);
        }
    }


    public void seedServicios() {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("seed/servicios.json");

            if (inputStream == null) {
                System.err.println("No se encontró seed/servicios.json");
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

    // Seed de Grupos

    public void seedGrupos() {
        try{
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("seed/grupos.json");

            if (inputStream == null) {
                System.err.println("No se encontró seed/grupos.json");
                return;
            }

            List<Grupo> grupos = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Grupo>>(){}
            );

            grupos.forEach(grupo -> {
                // Asignar role GRUPO a cada grupo
                grupo.setRole(Role.ROLE_GRUPO);
                grupo.setNombreCompleto(grupo.getClase());
                grupo.setContrasena(passwordEncoder.encode("1234"));
                grupo.setEmail("grupo" + grupo.getClase().toLowerCase() + "@gmail.com");
            });

            if (grupos.isEmpty()) {
                System.out.println("El JSON de grupos está vacío");
                return;
            }

            // Evitar duplicados por nombre
            long countBefore = grupoRepository.count();
            grupoRepository.saveAll(grupos);
            long countAfter = grupoRepository.count();

            System.out.println("Grupos importados: " + (countAfter - countBefore));

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON de grupos", e);
        }
    }
}
