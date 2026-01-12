package com.example.demo.controller;

import com.example.demo.model.Agenda;
import com.example.demo.payload.request.AgendaRequest;
import com.example.demo.service.AgendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    // Metodos GET
    // Obtener todas las agendas
    /*@GetMapping("/")
    public List<Agenda> getAll() {
        System.out.println("Controller: Obteniendo todas las agendas...");
        return agendaService.findAll();
    }
    */
    @GetMapping("/")
    public ResponseEntity<List<Agenda>> searchAgendas(
            @RequestParam(required = false) Long servicio,
            @RequestParam(required = false) Long grupo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate hasta
    ) {
        LocalDateTime desdeDateTime = (desde != null) ? desde.atStartOfDay() : null;
        LocalDateTime hastaDateTime = (hasta != null) ? hasta.atTime(23, 59, 59) : null;

        return ResponseEntity.ok(agendaService.search(servicio, grupo, desdeDateTime, hastaDateTime));
    }


    // Obtener una agenda por ID
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> getById(@PathVariable Long id) {
        Agenda agenda = agendaService.findById(id)
                .orElseThrow(()-> new RuntimeException("Agenda no encontrada"));
        return ResponseEntity.ok(agenda);
    }
    // Obtener agendas por grupo
    @GetMapping("/grupo/{grupoId}")
    public List<Agenda> getByGrupo(@PathVariable Long grupoId) {
        System.out.println("Controller: Obteniendo agendas para el grupo ID: " + grupoId);
        return agendaService.getAgendasByGrupo(grupoId);
    }
    // Obtener agendas por servicio
    @GetMapping("/servicio/{servicioId}")
    public List<Agenda> getByServicio(@PathVariable Long servicioId) {
        return agendaService.getAgendasByServicio(servicioId);
    }
    // Obtener agendas por grupo y servicio
    @GetMapping("/grupo/{grupoId}/servicio/{servicioId}")
    public List<Agenda> getByGrupoAndServicio(@PathVariable Long grupoId, @PathVariable Long servicioId) {
        return agendaService.getAgendasByGrupoAndServicio(grupoId, servicioId);
    }

    // Metodos POST
    // Crear una nueva agenda
    @PostMapping("/")
    public ResponseEntity<Agenda> create(@RequestBody AgendaRequest request) {
        Agenda nuevaAgenda = agendaService.create(request);
        return ResponseEntity.ok(nuevaAgenda);
    }

    // Metodos DELETE
    // Borrar una agenda por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
