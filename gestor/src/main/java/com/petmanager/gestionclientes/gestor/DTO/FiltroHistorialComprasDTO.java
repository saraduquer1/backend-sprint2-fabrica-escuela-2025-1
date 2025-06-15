package com.petmanager.gestionclientes.gestor.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class FiltroHistorialComprasDTO {

    private UUID idCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String categoria;
    private BigDecimal montoMinimo;
    private int limit = 10;
    private int offset = 0;
}
