package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaHora;

    // Una cita pertenece a un cliente
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Una cita pertenece a un servicio
    @ManyToOne(optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    // Una cita pertenece a un grupo (si tu modelo lo requiere)
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    public Cita(LocalDateTime fechaHora, Servicio servicio, Cliente cliente, Grupo grupo) {
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.cliente = cliente;
        this.grupo = grupo;
    }

    public LocalDateTime getHoraFinal() {
        return fechaHora.plusMinutes(servicio.getDuracion());
    }
}
