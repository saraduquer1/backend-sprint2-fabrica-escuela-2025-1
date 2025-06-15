package com.petmanager.gestionclientes.gestor.controller;

import com.petmanager.gestionclientes.gestor.DTO.*;
import com.petmanager.gestionclientes.gestor.model.Cliente;
import com.petmanager.gestionclientes.gestor.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // 1. Obtener todos los clientes
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteVistaDto>> obtenerTodos() {
        List<ClienteVistaDto> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    // 2. Obtener detalle de un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable UUID id) {
        try {
            ClienteDetalleDTO cliente = clienteService.getUsuario(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 3. Buscar clientes por distintos parámetros
    @PostMapping("/buscar")
    public ResponseEntity<?> buscarClientes(@RequestBody ClienteBusquedaDTO dto) {
        try {
            List<ClienteDetalleDTO> resultados = clienteService.buscarClientes(dto);
            return ResponseEntity.ok(resultados);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 4. Crear cliente con preferencias
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRegistroDTO dto, Integer usuario_id) {
        try {
            clienteService.CrearClienteConPreferencias(dto, usuario_id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar cliente.");
        }
    }

    // 5. Eliminar cliente con auditoría (requiere palabra clave BORRAR)
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarCliente(@RequestBody EliminarClienteDTO dto) {
        try {
            clienteService.eliminarClienteConAuditoria(dto);
            return ResponseEntity.ok("Cliente eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar cliente.");
        }
    }

    //6. Editar informacion del cliente con auditoria
    @PutMapping("/editar")
    public ResponseEntity<?> editarCliente(@RequestBody EditarClienteDTO dto) {
        try {
            clienteService.editarCliente(dto);
            return ResponseEntity.ok("Los datos fueron actualizados correctamente.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar los cambios: " + ex.getMessage());
        }
    }

    @PostMapping("/historial")
    public ResponseEntity<List<HistorialCompraDTO>> obtenerHistorial(@RequestBody FiltroHistorialComprasDTO filtro) {
        try {
            List<HistorialCompraDTO> historial = clienteService.obtenerHistorialCompras(filtro);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/reportes/clientes-frecuentes")
    public ResponseEntity<List<ClienteFrecuenteDTO>> obtenerReporte(@RequestParam String periodo) {

        LocalDate hoy = LocalDate.now();
        LocalDate fechaInicio;

        switch (periodo.toLowerCase()) {
            case "ultimo_mes":
                fechaInicio = hoy.minusMonths(1);
                break;
            case "ultimo_trimestre":
                fechaInicio = hoy.minusMonths(3);
                break;
            case "ultimo_año":
                fechaInicio = hoy.minusYears(1);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        List<ClienteFrecuenteDTO> resultado = clienteService.obtenerClientesFrecuentes(fechaInicio, hoy, 20);
        return ResponseEntity.ok(resultado);
    }
}
