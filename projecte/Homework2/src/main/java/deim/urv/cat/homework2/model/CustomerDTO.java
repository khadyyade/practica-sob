package deim.urv.cat.homework2.model;

import java.io.Serializable;

/**
 * Data Transfer Object para representar un cliente (customer).
 * 
 * RESPONSABILIDADES:
 * - Representar la estructura JSON/XML de clientes devuelta por la API REST
 * - NO es una entidad JPA, solo un POJO para transferencia de datos
 * 
 * ESTRUCTURA DE LA ENTIDAD Customer EN HOMEWORK1:
 * - id (Long)
 * - username (String) → obtenido de credentials.username
 * - telefono (String)
 * - ultimoModeloVisitado (Model) → solo se devuelve el ID y nombre como JSON anidado
 * 
 * EJEMPLO DE JSON DEVUELTO POR LA API (GET /customer/{id}):
 * {
 *   "id": 1,
 *   "username": "sob",
 *   "telefono": "123456789",
 *   "ultimoModeloVisitado": {
 *     "id": 3,
 *     "nombre": "GPT-4",
 *     "link": "/rest/api/v1/models/3"
 *   }
 * }
 * 
 * ASIGNADO A: Persona B
 */
public class CustomerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String telefono;
    private LastViewedModelInfo ultimoModeloVisitado;

    // ==================== CONSTRUCTORES ====================

    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LastViewedModelInfo getUltimoModeloVisitado() {
        return ultimoModeloVisitado;
    }

    public void setUltimoModeloVisitado(LastViewedModelInfo ultimoModeloVisitado) {
        this.ultimoModeloVisitado = ultimoModeloVisitado;
    }

    // ==================== CLASE INTERNA PARA MODELO VISITADO ====================

    /**
     * Información del último modelo visitado (formato HATEOAS de la API)
     */
    public static class LastViewedModelInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String nombre;
        private String link;

        public LastViewedModelInfo() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
