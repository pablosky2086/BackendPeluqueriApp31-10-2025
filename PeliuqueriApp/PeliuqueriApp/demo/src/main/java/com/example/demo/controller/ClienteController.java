package com.example.demo.controller;


import com.example.demo.model.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cliente> getAllClientes() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public ResponseEntity<Cliente> getClienteByid(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente no encontrado"));
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cliente> getClientesByNombre(@RequestParam String nombre) {
        return clienteService.findByPartialName(nombre);
    }

}
