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
@Table(name = "agendas")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime horaInicio;
    LocalDateTime horaFin;
    String aula;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    Servicio servicio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    Grupo grupo;

    public Agenda(LocalDateTime horaInicio, LocalDateTime horaFin, Servicio servicio, Grupo grupo, String aula) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.servicio = servicio;
        this.grupo = grupo;
        this.aula = aula;
    }
}
