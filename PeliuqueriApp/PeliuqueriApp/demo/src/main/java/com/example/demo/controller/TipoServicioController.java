package com.example.demo.controller;

import com.example.demo.model.TipoServicio;
import com.example.demo.service.TipoServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-servicio")
public class TipoServicioController {

    private final TipoServicioService tipoServicioService;

    public TipoServicioController(TipoServicioService tipoServicioService) {
        this.tipoServicioService = tipoServicioService;
    }

    // GET ALL
    @GetMapping("/")
    public List<TipoServicio> getAll() {
        return tipoServicioService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoServicio> getById(@PathVariable Long id) {
        return tipoServicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoServicio> create(@RequestBody TipoServicio tipoServicio) {
        TipoServicio creado = tipoServicioService.addTipoServicio(tipoServicio);
        return ResponseEntity.ok(creado);
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoServicio> update(
            @PathVariable Long id,
            @RequestBody TipoServicio tipoActualizado) {

        return tipoServicioService.update(id, tipoActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = tipoServicioService.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

