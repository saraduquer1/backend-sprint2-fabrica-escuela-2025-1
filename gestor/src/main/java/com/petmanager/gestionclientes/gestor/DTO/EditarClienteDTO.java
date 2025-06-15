package com.petmanager.gestionclientes.gestor.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class EditarClienteDTO {

    private UUID idCliente;
    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private String correoElectronico;
    private String telefono;
    private String direccion;
}
