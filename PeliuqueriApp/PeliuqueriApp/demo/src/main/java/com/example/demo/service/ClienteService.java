package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClienteRepository;
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

    public List<Cliente> findByAlergenosParcial(String alergenos) {
        return clienteRepository.findByAlergenosContaining(alergenos);
    }

    public List<Cliente> findByObservacionesParcial(String observaciones) {
        return clienteRepository.findByObservacionesContaining(observaciones);
    }

    public Optional<Cliente> update (Long id, Cliente cliente){
        Optional<Cliente> oldCliente = clienteRepository.findById(id);
        if (oldCliente.isEmpty()){
            return Optional.empty();
        }
        Cliente newCliente = (Cliente) oldCliente.get();
        if (cliente.getNombreCompleto()!=null) newCliente.setNombreCompleto(cliente.getNombreCompleto());
        if (cliente.getEmail()!=null) newCliente.setEmail(cliente.getEmail());
        if (cliente.getContrasena()!=null) newCliente.setContrasena(cliente.getContrasena());
        if (cliente.getTelefono()!=null) newCliente.setTelefono(cliente.getTelefono());
        if (cliente.getObservaciones()!=null) newCliente.setObservaciones(cliente.getObservaciones());
        if (cliente.getAlergenos()!=null) newCliente.setAlergenos(cliente.getAlergenos());

        clienteRepository.save(newCliente);

        return Optional.of(newCliente);
    }

    public Usuario save (Cliente cliente){return clienteRepository.save(cliente);}

    public void deleteById(Long id){clienteRepository.deleteById(id);}

}
