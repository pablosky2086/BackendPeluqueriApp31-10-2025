package com.example.demo.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCitaRequest {
    private Long clienteId;
    private Long agendaId;
    /* El parametro fechaHoraInicio es opcional */
    private String fechaHoraInicio;
}
