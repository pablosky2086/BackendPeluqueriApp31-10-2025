package com.example.demo.service;

import com.example.demo.model.Cita;
import com.example.demo.model.Cliente;
import com.example.demo.model.Grupo;
import com.example.demo.model.Servicio;
import com.example.demo.payload.request.NewCitaRequest;
import com.example.demo.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {
    private final CitaRepository citaRepository;
    private final ClienteService clienteService;
    private final GrupoService grupoService;
    private final ServicioService servicioService;

    public CitaService(CitaRepository citaRepository, ClienteService clienteService, GrupoService grupoService, ServicioService servicioService) {
        this.citaRepository = citaRepository;
        this.clienteService = clienteService;
        this.grupoService = grupoService;
        this.servicioService = servicioService;
    }

    //Metodos GET
    // Obtener todas las citas
    public List<Cita> findAll() {return citaRepository.findAll();}
    // Obtener una cita por ID
    public Optional<Cita> findById(Long id) { return citaRepository.findById(id); }

    // Obtener citas por cliente, grupo o servicio
    public List<Cita> getCitasByCliente(Long clienteId) {return citaRepository.findByClienteId(clienteId);}

    public List<Cita> getCitasByGrupo(Long grupoId) {return citaRepository.findByGrupoId(grupoId);}

    public List<Cita> getCitasByServicio(Long servicioId) {return citaRepository.findByServicioId(servicioId);}

    // Metodos PUT
    // Actualizar una cita
    public Optional<Cita> update (Long id, Cita cita){
        Optional<Cita> oldCita = citaRepository.findById(id);
        if (oldCita.isEmpty()){
            return Optional.empty();
        }
        Cita newCita = oldCita.get();
        if (cita.getFechaHora()!=null) newCita.setFechaHora(cita.getFechaHora());
        if (cita.getServicio()!=null) newCita.setServicio(cita.getServicio());
        if (cita.getCliente()!=null) newCita.setCliente(cita.getCliente());
        if (cita.getGrupo()!=null) newCita.setGrupo(cita.getGrupo());

        citaRepository.save(newCita);

        return Optional.of(newCita);
    }

    // Metodos POST
    // Crear una nueva cita
    public Cita save (Cita cita){return citaRepository.save(cita);}

    // Metodos DELETE
    // Cancelar una cita por ID
    public void cancelarPorId(Long id){citaRepository.deleteById(id);}

    public Cita create(NewCitaRequest request) {
        System.out.println("Creando nueva cita con los datos: " + request);
        Cliente cliente = clienteService.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Grupo grupo = grupoService.findById(request.getGrupoId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        Servicio servicio = servicioService.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        LocalDateTime fechaHora = LocalDateTime.parse(request.getFechaHora().toString());
        Cita cita = new Cita(fechaHora, servicio, cliente, grupo);
        return citaRepository.save(cita);
    }
}
