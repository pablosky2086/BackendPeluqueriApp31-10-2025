package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "servicios")
@ToString(callSuper = true)
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracion;
    private Double precio;

    //Relacion con Agenda OneToMany
    @OneToMany(mappedBy = "servicio")
    @JsonIgnore
    private List<Agenda> agendas;

    //Relacion con TipoServicio ManyToOne
    @ManyToOne
    @JoinColumn(name = "tipo_servicio_id")
    private TipoServicio tipoServicio;

    @ManyToMany(mappedBy = "servicios")
    private List<Grupo> grupos;

}
