package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @OneToMany(mappedBy = "agenda")
    @JsonIgnore
    private List<Cita> citas;

    public Agenda(LocalDateTime horaInicio, LocalDateTime horaFin, Servicio servicio, Grupo grupo, String aula) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.servicio = servicio;
        this.grupo = grupo;
        this.aula = aula;
    }

    // Calcular los huecos dentro de la agenda
    public List<LocalDateTime> calcularHuecos() {
        int duracionServicio = servicio.getDuracion();
        List<LocalDateTime> huecos = new ArrayList<>();
        for (LocalDateTime i = horaInicio;i.isBefore(horaFin.minusMinutes(duracionServicio)); i = i.plusMinutes(duracionServicio)) {
            huecos.add(i);
        }
        return huecos;
    }
}
