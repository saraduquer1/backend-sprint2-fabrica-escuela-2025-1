
# 🐾 PetManager - Gestión de Clientes

Este proyecto es un sistema backend desarrollado en **Spring Boot 3.4.5** para la gestión de clientes en una tienda de productos y servicios para mascotas. Incluye funcionalidades avanzadas como auditoría, filtros de historial de compras, JWT, monitoreo y despliegue con Docker.

---

## 📦 Contenido

- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Arquitectura y protocolos](#arquitectura-y-protocolos)
- [Ejecución con Docker](#ejecución-con-docker)
- [Autenticación y Seguridad](#autenticación-y-seguridad)
- [Demostración del uso de la API](#demostración-del-uso-de-la-api)
- [Monitoreo (Prometheus & Grafana)](#monitoreo-prometheus--grafana)
- [Endpoints disponibles](#endpoints-disponibles)

---

## 🛠️ Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.5
- Spring Security + JWT
- PostgreSQL
- Docker & Docker Compose
- Prometheus / Grafana
- Maven

---

## 📡 Arquitectura y protocolos

### Comunicación interna

- Basada en **HTTP RESTful API**.
- Seguridad basada en **JWT** con filtros personalizados (`JwtFiltroAutenticacion`).
- Roles admitidos: `ADMIN`, `VENDEDOR`, `MARKETING`.

### Comunicación externa

- Actualmente no hay integración con APIs externas, pero el sistema está preparado para ello mediante `RestTemplate` o `WebClient`.

---

## 🐳 Ejecución con Docker

### Requisitos

- Docker y Docker Compose instalados.

### Instrucciones

```bash
git clone https://github.com/tu-usuario/proyecto.git
cd proyecto
docker-compose up --build
```

Esto levanta:
- Backend (Spring Boot)
- Base de datos PostgreSQL
- Prometheus (monitoreo)
- Grafana (visualización)

---

## 🔐 Autenticación y Seguridad

La seguridad se maneja con JWT. Para autenticarte:

1. Usa `POST /auth/login` con:

```json
{
  "correo_electronico": "admin@petmanager.com",
  "contrasena": "1234"
}
```

2. Obtendrás un token:  
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5...
   ```

3. Usa ese token en las demás solicitudes bajo el header:  
   `Authorization: Bearer <token>`

---

## 🧪 Demostración del uso de la API

Puedes ver una demostración completa de los endpoints funcionando en el siguiente video:

📹 **Video demostrativo**: [Ver en Drive](https://drive.google.com/drive/folders/1GoAluWkThgeu6bMgODD9yhHX_5WYinB_?usp=sharing)

Este video muestra:

- Registro de clientes con preferencias
- Búsquedas por filtros
- Edición y eliminación con auditoría
- Historial de compras con paginación y filtros
- Reportes de clientes frecuentes

---

## 🔗 Endpoints disponibles

| Método | Endpoint                                 | Descripción |
|--------|------------------------------------------|-------------|
| POST   | `/auth/login`                            | Autenticación y generación de JWT |
| GET    | `/clientes/listar`                       | Listado de clientes |
| GET    | `/clientes/{id}`                         | Detalle de un cliente |
| POST   | `/clientes/buscar`                       | Buscar clientes por filtros |
| POST   | `/clientes/registrar`                    | Registrar cliente con preferencias |
| PUT    | `/clientes/editar`                       | Editar cliente (con auditoría) |
| DELETE | `/clientes/eliminar`                     | Eliminar cliente (con auditoría y confirmación) |
| POST   | `/clientes/historial`                    | Historial de compras con filtros (fecha, monto, categoría, paginación) |
| GET    | `/clientes/reportes/clientes-frecuentes?periodo=ultimo_mes` | Reporte de clientes frecuentes en el ultimo, tambien se puede ver el reporte del ulitmo trimestre y año |

---

## 📞 Contacto

Desarrollado por: [Sebastian Amaya Perez, Dorian Jaramillo] 
---
