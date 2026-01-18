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

    // Metodos GET
    // Obtener todas las agendas
    public List<AgendaResponseDTO> findAll() {
        System.out.println("Servicio: Obteniendo todas las agendas...");
        return agendaRepository.findAll().stream().map(AgendaService::toDTO).toList();
    }
    // Obtener una agenda por ID
    public Optional<AgendaResponseDTO> findById(Long id) {
        Optional<Agenda> agendaOpt = agendaRepository.findById(id);
        return agendaOpt.map(AgendaService::toDTO);
    }

    // Obtener agendas por grupo o servicio
    public List<AgendaResponseDTO> getAgendasByGrupo(Long grupoId) {
        return agendaRepository.findByGrupoId(grupoId).stream().map(AgendaService::toDTO).toList();
    }
    public List<AgendaResponseDTO> getAgendasByServicio(Long servicioId) {
        return agendaRepository.findByServicioId(servicioId).stream().map(AgendaService::toDTO).toList();
    }

    // Obtener agendas por grupo y servicio

    public List<AgendaResponseDTO> getAgendasByGrupoAndServicio(Long grupoId, Long servicioId) {
        return agendaRepository.findByGrupoIdAndServicioId(grupoId, servicioId).stream().map(AgendaService::toDTO).toList();
    }





    // Metodos POST
    // Crear una nueva agenda
    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    // Metodos DELETE
    public void deleteById(Long id) {
        agendaRepository.deleteById(id);
    }

    // Metodos Complemetarios
    // Crear una nueva agenda a partir de un request
    public Agenda create(AgendaRequest request){
        System.out.println("Creando nueva agenda con los datos: " + request);
        Grupo grupo = grupoService.findById(request.getGrupoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));
        Servicio servicio = servicioService.findById(request.getServicioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
        LocalDateTime horaInicio = LocalDateTime.parse(request.getHoraInicio());
        LocalDateTime horaFin = LocalDateTime.parse(request.getHoraFin());
        String aula = request.getAula();
        int sillas = request.getSillas();
        Agenda agenda = new Agenda(horaInicio, horaFin, servicio, grupo, aula,sillas);
        return agendaRepository.save(agenda);
    }

    // Crear agendas para un tiempo
    public void createAgendasParaUnTiempo(AgendaRequest request, int numeroDeAgendas){
        for (int i = 0; i < numeroDeAgendas; i++) {
            Grupo grupo = grupoService.findById(request.getGrupoId()).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            Servicio servicio = servicioService.findById(request.getServicioId()).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            LocalDateTime horaInicio = LocalDateTime.parse(request.getHoraInicio()).plusDays(i*7);
            LocalDateTime horaFin = LocalDateTime.parse(request.getHoraFin()).plusDays(i*7);
            String aula = request.getAula();
            int sillas = request.getSillas();
            Agenda agenda = new Agenda(horaInicio, horaFin, servicio, grupo, aula, sillas);
            agendaRepository.save(agenda);
        }
        System.out.println("Se han creado " + numeroDeAgendas + " agendas semanalmente a partir de la fecha " + request.getHoraInicio());
    }

    public List<AgendaResponseDTO> search(Long servicio, Long grupo, LocalDateTime desde, LocalDateTime hasta) {
        List<Agenda> agendas = agendaRepository.findAll();
        if (servicio != null) {
            agendas = agendas.stream()
                    .filter(a -> a.getServicio().getId().equals(servicio))
                    .toList();
        }
        if (grupo != null) {
            agendas = agendas.stream()
                    .filter(a -> a.getGrupo().getId().equals(grupo))
                    .toList();
        }
        if (desde != null) {
            agendas = agendas.stream()
                    .filter(a -> !a.getHoraInicio().isBefore(desde))
                    .toList();
        }
        if (hasta != null) {
            agendas = agendas.stream()
                    .filter(a -> !a.getHoraFin().isAfter(hasta))
                    .toList();
        }
        return agendas.stream().map(AgendaService::toDTO).toList();
    }
    // Obtener dias disponibles para un servicio apartir de las agendas
    public List<LocalDate> getDiasDisponiblesParaServicio(Long servicioId, Long grupo, LocalDateTime desde, LocalDateTime hasta) {
       List <AgendaResponseDTO> agendas = search(servicioId, grupo, desde, hasta);
       List <LocalDate> diasDisponibles = agendas.stream()
               .map(AgendaResponseDTO::getHoraInicio)
               .map(LocalDateTime::toLocalDate)
               .distinct()
               .toList();
         return diasDisponibles;
    }

    // Mapper de Agenda a AgendaResponseDTO

    private static AgendaResponseDTO toDTO(Agenda agenda) {

        return new AgendaResponseDTO(
                agenda.getId(),
                agenda.getHoraInicio(),
                agenda.getHoraFin(),
                agenda.getAula(),
                agenda.getSillas(),
                agenda.getServicio(),
                agenda.getGrupo(),
                agenda.calcularHuecos()
        );
    }

}
