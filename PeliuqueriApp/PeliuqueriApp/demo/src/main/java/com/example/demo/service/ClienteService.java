package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.Cliente;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService extends UsuarioService{
    private final ClienteRepository clienteRepository;

    public ClienteService(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        super(usuarioRepository);
        this.clienteRepository = clienteRepository;
    }

    public Optional<Cliente> update (Long id, Cliente cliente){
        Optional<Usuario> oldCliente = clienteRepository.findById(id);
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
