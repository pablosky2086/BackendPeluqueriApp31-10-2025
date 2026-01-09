package com.example.demo.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgendaRequest {
    private String horaInicio;
    private String horaFin;
    private Long grupoId;
    private Long servicioId;
    private String aula;
}
