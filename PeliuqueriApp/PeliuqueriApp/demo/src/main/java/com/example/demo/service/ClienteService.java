package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService( ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) { return clienteRepository.findById(id); }

    public List<Cliente> findByPartialName(String nombre) { return clienteRepository.findByNombre_completoContaining(nombre); }

    public Optional<Cliente> update (Long id, Cliente cliente){
        Optional<Cliente> oldCliente = clienteRepository.findById(id);
        if (oldCliente.isEmpty()){
            return Optional.empty();
        }
        Cliente newCliente = (Cliente) oldCliente.get();
        if (cliente.getNombre_completo()!=null) newCliente.setNombre_completo(cliente.getNombre_completo());
        if (cliente.getEmail()!=null) newCliente.setEmail(cliente.getEmail());
        if (cliente.getContrasena()!=null) newCliente.setContrasena(cliente.getContrasena());
        if (cliente.getTelefono()!=null) newCliente.setTelefono(cliente.getTelefono());
        if (cliente.getObservaciones()!=null) newCliente.setObservaciones(cliente.getObservaciones());
        if (cliente.getAlergenos()!=null) newCliente.setAlergenos(cliente.getAlergenos());

        clienteRepository.save(newCliente);

        return Optional.of(newCliente);
    }

    public Usuario save (Cliente cliente){return clienteRepository.save(cliente);}
}
