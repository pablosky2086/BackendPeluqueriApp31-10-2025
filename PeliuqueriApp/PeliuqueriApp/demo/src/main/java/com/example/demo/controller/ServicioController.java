package com.example.demo.controller;

import com.example.demo.model.Servicio;
import com.example.demo.service.ServicioService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/servicios")
public class ServicioController {
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping("/")
    public List<Servicio> getAllServicios(){return servicioService.findAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> findServicioById(@PathVariable Long id){
        Servicio servicio = servicioService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Servicio no encontrado"));
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public List<Servicio> findServicioByNombreParcial(@PathVariable String nombre){return servicioService.findServiciosByNombreParcial(nombre);}

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicio> addServicio(@RequestBody Servicio servicio){
        Servicio addedServicio = servicioService.save(servicio);
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicio> updateServicio(@PathVariable Long id, @RequestBody Servicio servicio){
        Servicio uppdatedServicio = servicioService.update(id,servicio)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Servicio no encontrado"));
        return new ResponseEntity<Servicio>(uppdatedServicio, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteServicio(@PathVariable long id){
        servicioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
