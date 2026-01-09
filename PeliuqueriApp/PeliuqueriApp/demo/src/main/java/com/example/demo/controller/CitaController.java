package com.example.demo.controller;

import com.example.demo.model.Cita;
import com.example.demo.payload.request.NewCitaRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.CitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/citas")
public class CitaController {

    private final CitaService citaService;
    private final AuthService authService;

    public CitaController(CitaService citaService, AuthService authService) {
        this.citaService = citaService;
        this.authService = authService;
    }

    // Metodos GET
    // Obtener todas las citas
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cita> getAll() {
        return citaService.findAll();
    }

    // Obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getById(@PathVariable Long id) {
        Cita cita = citaService.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
        if (!authService.isAdmin() && !authService.isGrupo() && cita.getCliente().getId() != authService.getCurrentUserId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a este recurso");
        } else {
            return new ResponseEntity<Cita>(cita, HttpStatus.OK);
        }
    }

    // Obtener citas por cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Cita> getByCliente(@PathVariable Long clienteId) {
        if (!authService.isAdmin() && !authService.isGrupo() && !authService.isOwnerOfCliente(clienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a este recurso");
        }
        return citaService.getCitasByCliente(clienteId);
    }

    // Obtener citas por grupo
    @GetMapping("/grupo/{grupoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cita> getByGrupo(@PathVariable Long grupoId) {
        return citaService.getCitasByGrupo(grupoId);
    }

    // Obtener citas por servicio
    @GetMapping("/servicio/{servicioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cita> getByServicio(@PathVariable Long servicioId) {
        return citaService.getCitasByServicio(servicioId);
    }

    // POST - Crear nueva cita
    @PostMapping("/")
    public ResponseEntity<Cita> create(@RequestBody NewCitaRequest request) {
        System.out.println("La peticion de crear cita es: " + request);
        if (!authService.isAdmin() && !authService.isGrupo() && request.getClienteId() != authService.getCurrentUserId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para crear una cita para este cliente");
        }
        Cita nuevaCita = citaService.create(request);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    // PUT - Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<Cita> update(@PathVariable Long id, @RequestBody Cita cita) {
        if (!authService.isAdmin() && !authService.isGrupo() && cita.getCliente().getId() != authService.getCurrentUserId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para actualizar esta cita");
        }
        return citaService.update(id, cita)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Cancelar una cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Cita cita = citaService.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
        if (!authService.isAdmin() && !authService.isGrupo() && cita.getCliente().getId() != authService.getCurrentUserId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para cancelar esta cita");
        }
        citaService.cancelarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
