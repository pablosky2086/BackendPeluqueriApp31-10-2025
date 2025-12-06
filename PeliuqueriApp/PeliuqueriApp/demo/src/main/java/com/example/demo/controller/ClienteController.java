package com.example.demo.controller;


import com.example.demo.model.Cliente;
import com.example.demo.model.Servicio;
import com.example.demo.service.ClienteService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<Cliente> getClienteByid(@PathVariable Long id) {
        // Obtener el objeto Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Obtener los detalles del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Obtener el rol y el email del usuario autenticado
        String userRole = userDetails.getAuthorities().stream().findFirst().map(g -> g.getAuthority()).orElse(null);
        String userEmail = userDetails.getUsername();
        // En caso de que el rol sea CLIENTE, verificar que el id del cliente solicitado coincida con el email del usuario autenticado
        if (userRole.equals("ROLE_CLIENTE")) {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (clienteOpt.isEmpty() || !clienteOpt.get().getEmail().equals(userEmail)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
            }
        }
        Cliente cliente = clienteService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente no encontrado"));
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @GetMapping("/alergenos/{alergeno}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cliente> getClientesByAlergeno(@PathVariable String alergeno){
        return clienteService.findByAlergenosParcial(alergeno);
    }

    @GetMapping("/observaciones/{observacion}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GRUPO')")
    public List<Cliente> getClientesByObservacion(@PathVariable String observacion){
        return clienteService.findByObservacionesParcial(observacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente){
        Cliente uppdatedCliente = clienteService.update(id,cliente)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente no encontrado"));
        return new ResponseEntity<Cliente>(uppdatedCliente, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteCliente(@PathVariable long id){
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
