package com.example.demo.service;

import com.example.demo.model.Agenda;
import com.example.demo.model.Grupo;
import com.example.demo.model.Servicio;
import com.example.demo.payload.DTOs.AgendaResponseDTO;
import com.example.demo.payload.request.AgendaRequest;
import com.example.demo.repository.AgendaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final GrupoService grupoService;
    private final ServicioService servicioService;

    public AgendaService(AgendaRepository agendaRepository, @Lazy GrupoService grupoService, @Lazy ServicioService servicioService) {
        this.agendaRepository = agendaRepository;
        this.grupoService = grupoService;
        this.servicioService = servicioService;
    }

    // --- MÉTODOS GET ---
    public List<AgendaResponseDTO> findAll() {
        System.out.println("Servicio: Obteniendo todas las agendas...");
        return agendaRepository.findAll().stream().map(AgendaService::toDTO).toList();
    }

    public Optional<AgendaResponseDTO> findById(Long id) {
        Optional<Agenda> agendaOpt = agendaRepository.findById(id);
        return agendaOpt.map(AgendaService::toDTO);
    }

    public List<AgendaResponseDTO> getAgendasByGrupo(Long grupoId) {
        return agendaRepository.findByGrupoId(grupoId).stream().map(AgendaService::toDTO).toList();
    }

    public List<AgendaResponseDTO> getAgendasByServicio(Long servicioId) {
        return agendaRepository.findByServicioId(servicioId).stream().map(AgendaService::toDTO).toList();
    }

    public List<AgendaResponseDTO> getAgendasByGrupoAndServicio(Long grupoId, Long servicioId) {
        return agendaRepository.findByGrupoIdAndServicioId(grupoId, servicioId).stream().map(AgendaService::toDTO).toList();
    }

    // --- MÉTODOS POST / CREATE ---
    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public Agenda create(AgendaRequest request) {
        System.out.println("Creando nueva agenda con los datos: " + request);
        Grupo grupo = grupoService.findById(request.getGrupoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));
        Servicio servicio = servicioService.findById(request.getServicioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        LocalDateTime horaInicio = LocalDateTime.parse(request.getHoraInicio());
        LocalDateTime horaFin = LocalDateTime.parse(request.getHoraFin());

        Agenda agenda = new Agenda(horaInicio, horaFin, servicio, grupo, request.getAula(), request.getSillas());
        return agendaRepository.save(agenda);
    }

    // --- MÉTODOS PUT / UPDATE (NUEVO) ---
    public Agenda update(Long id, AgendaRequest request) {
        System.out.println("Actualizando agenda ID " + id + " con datos: " + request);

        // 1. Buscar la agenda existente
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agenda no encontrada con ID: " + id));

        // 2. Validar que el grupo y servicio nuevos existan
        Grupo grupo = grupoService.findById(request.getGrupoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));
        Servicio servicio = servicioService.findById(request.getServicioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));

        // 3. Actualizar los campos
        agenda.setHoraInicio(LocalDateTime.parse(request.getHoraInicio()));
        agenda.setHoraFin(LocalDateTime.parse(request.getHoraFin()));
        agenda.setServicio(servicio);
        agenda.setGrupo(grupo);
        agenda.setAula(request.getAula());
        agenda.setSillas(request.getSillas());

        // 4. Guardar cambios
        return agendaRepository.save(agenda);
    }

    // --- MÉTODOS DELETE ---
    public void deleteById(Long id) {
        agendaRepository.deleteById(id);
    }

    // --- MÉTODOS COMPLEMENTARIOS ---
    public void createAgendasParaUnTiempo(AgendaRequest request, int numeroDeAgendas) {
        for (int i = 0; i < numeroDeAgendas; i++) {
            Grupo grupo = grupoService.findById(request.getGrupoId()).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            Servicio servicio = servicioService.findById(request.getServicioId()).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            LocalDateTime horaInicio = LocalDateTime.parse(request.getHoraInicio()).plusDays(i * 7);
            LocalDateTime horaFin = LocalDateTime.parse(request.getHoraFin()).plusDays(i * 7);
            Agenda agenda = new Agenda(horaInicio, horaFin, servicio, grupo, request.getAula(), request.getSillas());
            agendaRepository.save(agenda);
        }
    }

    public List<AgendaResponseDTO> search(Long servicio, Long grupo, LocalDateTime desde, LocalDateTime hasta) {
        List<Agenda> agendas = agendaRepository.findAll();
        if (servicio != null) {
            agendas = agendas.stream().filter(a -> a.getServicio().getId().equals(servicio)).toList();
        }
        if (grupo != null) {
            agendas = agendas.stream().filter(a -> a.getGrupo().getId().equals(grupo)).toList();
        }
        if (desde != null) {
            agendas = agendas.stream().filter(a -> !a.getHoraInicio().isBefore(desde)).toList();
        }
        if (hasta != null) {
            agendas = agendas.stream().filter(a -> !a.getHoraFin().isAfter(hasta)).toList();
        }
        return agendas.stream().map(AgendaService::toDTO).toList();
    }

    public List<LocalDate> getDiasDisponiblesParaServicio(Long servicioId, Long grupo, LocalDateTime desde, LocalDateTime hasta) {
        List<AgendaResponseDTO> agendas = search(servicioId, grupo, desde, hasta);
        return agendas.stream()
                .map(AgendaResponseDTO::getHoraInicio)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .toList();
    }

    // --- MAPPER ---
    private static AgendaResponseDTO toDTO(Agenda agenda) {
        return new AgendaResponseDTO(
                agenda.getId(),
                agenda.getHoraInicio(),
                agenda.getHoraFin(),
                agenda.getAula(),
                agenda.getSillas(),
                agenda.getServicio(),
                agenda.getGrupo(),
                agenda.horasDisponiblesConEstado()
        );
    }
}