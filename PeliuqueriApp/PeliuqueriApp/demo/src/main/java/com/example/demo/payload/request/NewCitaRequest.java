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
    private String fechaHora;
    private Long clienteId;
    private Long servicioId;
    private Long grupoId;
}
