package com.petmanager.gestionclientes.gestor.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ClienteFrecuenteDTO {

    private UUID idCliente;
    private String nombre;
    private String correoElectronico;
    private Integer totalCompras;
    private List<String> productosComprados;
}
