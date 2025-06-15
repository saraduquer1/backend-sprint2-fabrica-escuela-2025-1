package com.petmanager.gestionclientes.gestor.service;
import com.petmanager.gestionclientes.gestor.DTO.*;
import com.petmanager.gestionclientes.gestor.model.Cliente;
import com.petmanager.gestionclientes.gestor.repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public List<ClienteVistaDto> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::ClienteToVIstaDTO)
                .collect(Collectors.toList());
    }

    public ClienteDetalleDTO getUsuario(UUID id) throws Exception {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));
        return ClienteToDetalleDTO(cliente);
    }

    public List<ClienteDetalleDTO> buscarClientes(ClienteBusquedaDTO dto) {
        List<Object[]> resultados = entityManager
                .createNativeQuery("SELECT * FROM buscar_clientes(:id, :nombre, :apellidos, :correo)")
                .setParameter("id", dto.getId())
                .setParameter("nombre", dto.getNombre())
                .setParameter("apellidos", dto.getApellidos())
                .setParameter("correo", dto.getCorreoElectronico())
                .getResultList();
        if (resultados.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron clientes con los criterios proporcionados.");
        }
        return resultados.stream()
                .map(row -> new ClienteDetalleDTO(
                        ((UUID) row[0]),  // id
                        (String) row[1],                // nombre
                        (String) row[2],                // apellido
                        (String) row[3],                // correo
                        (String) row[4],                // teléfono
                        (String) row[5]                 // dirección
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void CrearClienteConPreferencias(ClienteRegistroDTO dto, Integer usuario_id) {
        // Validar si el correo ya existe
        if (clienteRepository.findByCorreoElectronico(dto.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        String categoriasCsv = dto.getCategorias() == null || dto.getCategorias().isEmpty()
                ? ""  // si no hay categorías enviamos string vacío
                : dto.getCategorias().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));


        entityManager.createNativeQuery("SELECT crear_cliente_con_preferencias_2(:nombre, :apellidos, :correo, :telefono, :direccion, :categorias, :usuarioId)")
                .setParameter("nombre", dto.getNombre())
                .setParameter("apellidos", dto.getApellidos())
                .setParameter("correo", dto.getCorreoElectronico())
                .setParameter("telefono", dto.getTelefono())
                .setParameter("direccion", dto.getDireccion())
                .setParameter("categorias", categoriasCsv) // Ahora string CSV
                .setParameter("usuarioId", usuario_id)
                .getSingleResult();
    }


    public void eliminarClienteConAuditoria(EliminarClienteDTO dto) {
        if (!"BORRAR".equalsIgnoreCase(dto.getConfirmacion())) {
            throw new IllegalArgumentException("La palabra clave de confirmación es incorrecta.");
        }

        entityManager.createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)")
                .setParameter("idCliente", dto.getIdCliente())
                .setParameter("idUsuario", dto.getIdUsuario())
                .getSingleResult();  // Ejecuta la función de eliminación con auditoría
    }


    public void editarCliente(EditarClienteDTO dto) {
        // Validación de correo electrónico
        if (!dto.getCorreoElectronico().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }
        // Validación de teléfono nacional (puedes ajustar la expresión según el país)
        if (!dto.getTelefono().matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("El número de teléfono debe tener 10 dígitos.");
        }

        try {
            entityManager.createNativeQuery("SELECT actualizar_cliente_con_auditoria(:idCliente, :nombre, :apellidos, :correo, :telefono, :direccion, :idUsuario)")
                    .setParameter("idCliente", dto.getIdCliente())
                    .setParameter("nombre", dto.getNombre())
                    .setParameter("apellidos", dto.getApellidos())
                    .setParameter("correo", dto.getCorreoElectronico())
                    .setParameter("telefono", dto.getTelefono())
                    .setParameter("direccion", dto.getDireccion())
                    .setParameter("idUsuario", dto.getIdUsuario())
                    .getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();  // Esto imprimirá el error exacto del procedimiento
            throw new RuntimeException("No se pudieron guardar los cambios: " + e.getMessage());
        }
    }

    public List<HistorialCompraDTO> obtenerHistorialCompras(FiltroHistorialComprasDTO filtro) {

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT * FROM obtener_historial_compras2(:idCliente, :fechaInicio, :fechaFin, :categoria, :montoMinimo, :limit, :offset)")
                .setParameter("idCliente", filtro.getIdCliente())  // UUID
                .setParameter("fechaInicio",
                        filtro.getFechaInicio() != null ? java.sql.Date.valueOf(filtro.getFechaInicio()) : null)
                .setParameter("fechaFin",
                        filtro.getFechaFin() != null ? java.sql.Date.valueOf(filtro.getFechaFin()) : null)
                .setParameter("categoria", filtro.getCategoria()) // String
                .setParameter("montoMinimo", filtro.getMontoMinimo()) // BigDecimal o Double
                .setParameter("limit", filtro.getLimit()) // Integer
                .setParameter("offset", filtro.getOffset()) // Integer
                .getResultList();

        List<HistorialCompraDTO> compras = new ArrayList<>();
        for (Object[] row : results) {
            HistorialCompraDTO dto = new HistorialCompraDTO();
            dto.setCompraId((Integer) row[0]);
            dto.setFechaCompra(((Date) row[1]).toLocalDate());
            dto.setMontoTotal((BigDecimal) row[2]);
            dto.setProductoId((Integer) row[3]);
            dto.setNombreProducto((String) row[4]);
            dto.setCategoria((String) row[5]);
            dto.setUnidades((Integer) row[6]);
            dto.setPrecioUnitario((BigDecimal) row[7]);
            compras.add(dto);
        }
        return compras;
    }

    public List<ClienteFrecuenteDTO> obtenerClientesFrecuentes(LocalDate fechaInicio, LocalDate fechaFin, int limite) {

        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT * FROM obtener_clientes_frecuentes_con_productos(:fechaInicio, :fechaFin, :limite)")
                .setParameter("fechaInicio", java.sql.Date.valueOf(fechaInicio))
                .setParameter("fechaFin", java.sql.Date.valueOf(fechaFin))
                .setParameter("limite", limite)
                .getResultList();

        List<ClienteFrecuenteDTO> clientes = new ArrayList<>();

        for (Object[] row : results) {
            ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
            dto.setIdCliente((UUID) row[0]);
            dto.setNombre((String) row[1]);
            dto.setCorreoElectronico((String) row[2]);
            dto.setTotalCompras((Integer) row[3]);

            String productosString = (String) row[4];
            if (productosString != null && !productosString.isEmpty()) {
                List<String> productos = Arrays.stream(productosString.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
                dto.setProductosComprados(productos);
            } else {
                dto.setProductosComprados(Collections.emptyList());
            }

            clientes.add(dto);
        }
        return clientes;
    }




    private ClienteVistaDto ClienteToVIstaDTO(Cliente cliente) {
        ClienteVistaDto clienteVistaDto = new ClienteVistaDto();
        clienteVistaDto.setId(cliente.getId());
        clienteVistaDto.setNombre(cliente.getNombre());
        clienteVistaDto.setApellido(cliente.getApellido());
        return clienteVistaDto;
    }

    private ClienteDetalleDTO ClienteToDetalleDTO(Cliente cliente) {
        ClienteDetalleDTO clienteDetalleDTO = new ClienteDetalleDTO();
        clienteDetalleDTO.setId(cliente.getId());
        clienteDetalleDTO.setNombre(cliente.getNombre());
        clienteDetalleDTO.setApellido(cliente.getApellido());
        clienteDetalleDTO.setCorreoElectronico(cliente.getCorreoElectronico());
        clienteDetalleDTO.setTelefono(cliente.getTelefono());
        clienteDetalleDTO.setDireccion(cliente.getDireccion());
        return clienteDetalleDTO;
    }
}
