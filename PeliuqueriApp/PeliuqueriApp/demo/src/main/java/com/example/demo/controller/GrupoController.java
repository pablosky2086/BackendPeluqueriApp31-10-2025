package com.example.demo.controller;


import com.example.demo.model.Admin;
import com.example.demo.model.Grupo;
import com.example.demo.model.Turno;
import com.example.demo.service.GrupoService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Object getAllGrupos() {return grupoService.findAll();}

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable Long id){
        Grupo grupo = grupoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Grupo no encontrado"));
        return new ResponseEntity<Grupo>(grupo, HttpStatus.OK);
    }

    /*
    @GetMapping("/turno/{turno}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Grupo> getGruposByTurno(@PathVariable String turno){
        System.out.println("Turno recibido: " + turno);
        return grupoService.findByTurno(turno);
    }
     */

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Grupo> updateGrupo(@PathVariable Long id, @RequestBody Grupo grupo){
        Grupo updatedGrupo = grupoService.update(id,grupo)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Grupo no encontrado"));
        return new ResponseEntity<Grupo>(updatedGrupo, HttpStatus.OK);
    }

    @PostMapping("/{grupoId}/servicios/{servicioId}")
    public ResponseEntity<Grupo> addServicio(
            @PathVariable Long grupoId,
            @PathVariable Long servicioId) {

        boolean added = grupoService.addServicioToGrupo(grupoId, servicioId);

        return added
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response> deleteGrupo(@PathVariable Long id){
        grupoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
