package com.example.demo.service;

import com.example.demo.model.Agenda;
import com.example.demo.model.Grupo;
import com.example.demo.model.Servicio;
import com.example.demo.payload.request.AgendaRequest;
import com.example.demo.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final GrupoService grupoService;
    private final ServicioService servicioService;

    public AgendaService(AgendaRepository agendaRepository, GrupoService grupoService, ServicioService servicioService) {
        this.agendaRepository = agendaRepository;
        this.grupoService = grupoService;
        this.servicioService = servicioService;
    }

    // Metodos GET
    // Obtener todas las agendas
    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }
    // Obtener una agenda por ID
    public Optional<Agenda> findById(Long id) {
        return agendaRepository.findById(id);
    }

    // Obtener agendas por grupo o servicio
    public List<Agenda> getAgendasByGrupo(Long grupoId) {
        return agendaRepository.findByGrupoId(grupoId);
    }
    public List<Agenda> getAgendasByServicio(Long servicioId) {
        return agendaRepository.findByServicioId(servicioId);
    }

    // Obtener agendas por grupo y servicio

    public List<Agenda> getAgendasByGrupoAndServicio(Long grupoId, Long servicioId) {
        return agendaRepository.findByGrupoIdAndServicioId(grupoId, servicioId);
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
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        Servicio servicio = servicioService.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        LocalDateTime horaInicio = LocalDateTime.parse(request.getHoraInicio());
        LocalDateTime horaFin = LocalDateTime.parse(request.getHoraFin());
        String aula = request.getAula();
        Agenda agenda = new Agenda(horaInicio, horaFin, servicio, grupo, aula);
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
            Agenda agenda = new Agenda(horaInicio, horaFin, servicio, grupo, aula);
            agendaRepository.save(agenda);
        }
        System.out.println("Se han creado " + numeroDeAgendas + " agendas semanalmente a partir de la fecha " + request.getHoraInicio());
    }

}
