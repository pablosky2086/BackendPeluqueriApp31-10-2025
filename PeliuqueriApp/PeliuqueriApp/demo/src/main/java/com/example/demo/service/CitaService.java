package com.example.demo.service;

import com.example.demo.model.Agenda;
import com.example.demo.model.Cita;
import com.example.demo.model.Cliente;
import com.example.demo.model.Servicio;
import com.example.demo.payload.request.NewCitaRequest;
import com.example.demo.repository.AgendaRepository;
import com.example.demo.repository.CitaRepository;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final AgendaRepository agendaRepository;
    private final ClienteService clienteService;

    public CitaService(CitaRepository citaRepository,
                       AgendaRepository agendaRepository,
                       ClienteService clienteService) {
        this.citaRepository = citaRepository;
        this.agendaRepository = agendaRepository;
        this.clienteService = clienteService;
    }

    // ---------------- GET ----------------

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> getCitasByCliente(Long clienteId) {
        return citaRepository.findByClienteId(clienteId);
    }

    public List<Cita> getCitasByGrupo(Long grupoId) {
        return citaRepository.findByAgendaGrupoId(grupoId);
    }

    public List<Cita> getCitasByServicio(Long servicioId) {
        return citaRepository.findByAgendaServicioId(servicioId);
    }

    public List<Cita> getCitasEntreFechas(LocalDateTime desde, LocalDateTime hasta) {
        return citaRepository.findByAgendaHoraInicioBetween(desde, hasta);
    }

    public List<Cita> getCitasGrupoEntreFechas(
            Long grupoId,
            LocalDateTime desde,
            LocalDateTime hasta) {

        return citaRepository
                .findByAgendaGrupoIdAndAgendaHoraInicioBetween(grupoId, desde, hasta);
    }

    // ---------------- POST ----------------

    public Cita create(NewCitaRequest request) {

        /* Verificar que la agenda no tenga ya una cita */
        /* Sólo se permite una cita por agenda */
        /* A MODIFICAR si se permiten multiples citas por agenda */
        if (citaRepository.existsByAgendaId(request.getAgendaId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La agenda ya tiene una cita"
            );
        }

        /* Buscar la agenda y el cliente, si no los encuentra lanza un 404 */
        Agenda agenda = agendaRepository.findById(request.getAgendaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agenda no encontrada"));

        Cliente cliente = clienteService.findById(request.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        /* Extraer la hora de inicio de la cita del request */
        /* Si no se proporciona, usar la hora de inicio de la agenda */
        LocalDateTime inicio = request.getFechaHoraInicio() != null ?
                LocalDateTime.parse(request.getFechaHoraInicio())
                : agenda.getHoraInicio();

        /* Si la hora de la cita es antes de la agenda lanza un 400 */
        if (inicio.isBefore(agenda.getHoraInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La hora de inicio de la cita no puede ser antes que la hora de inicio de la agenda"
            );
        }
        /* Calcular la hora de fin de la cita */
        /* Si la hora de fin de la cita excede la hora de fin de la agenda lanza un 400 */
        Servicio servicio = agenda.getServicio();
        LocalDateTime finCita = inicio.plusMinutes(servicio.getDuracion());
        if (finCita.isAfter(agenda.getHoraFin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La hora de fin de la cita excede la hora de fin de la agenda"
            );
        }

        Cita cita = new Cita(inicio , agenda, cliente);
        return citaRepository.save(cita);
    }

    // ---------------- DELETE ----------------

    public void cancelarPorId(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cita no encontrada"
            );
        }
        citaRepository.deleteById(id);
    }
}
