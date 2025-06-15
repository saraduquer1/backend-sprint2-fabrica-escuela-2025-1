package com.petmanager.gestionclientes.gestor.DTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HistorialCompraDTO {

    private Integer compraId;
    private LocalDate fechaCompra;
    private BigDecimal montoTotal;
    private Integer productoId;
    private String nombreProducto;
    private String categoria;
    private int unidades;
    private BigDecimal precioUnitario;
}
