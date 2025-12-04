package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipos_servicio")
public class TipoServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String url_img;

    //Relacion con Servicio OneToMany
    @OneToMany(mappedBy = "tipoServicio", cascade = CascadeType.ALL)
    private List<Servicio> servicios = new ArrayList<>();
}
