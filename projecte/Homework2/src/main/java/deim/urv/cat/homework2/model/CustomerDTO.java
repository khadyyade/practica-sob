package deim.urv.cat.homework2.model;

import java.io.Serializable;

/**
 * Este objeto sirve para guardar los datos de un cliente que nos llegan de la API.
 * No es una entidad de base de datos, solo un "contenedor" para pasar información.
 * 
 * Cuando llamamos a la API de Homework1 para pedir info de un cliente,
 * la respuesta JSON la convertimos a este objeto para trabajar más fácil.
 * 
 */
public class CustomerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // El ID único del cliente en la base de datos
    private Long id;
    
    // El nombre de usuario (el que usa para hacer login)
    private String username;
    
    // Su número de teléfono
    private String telefono;
    
    // Info del último modelo de IA que visitó (puede ser null si no ha visto ninguno)
    private LastViewedModelInfo ultimoModeloVisitado;

    // CONSTRUCTORES

    // Constructor vacío: lo necesita Jakarta para crear el objeto desde JSON
    public CustomerDTO() {
    }

    // Constructor básico por si queremos crear uno rápido con id y username
    public CustomerDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // GETTERS Y SETTERS
    // Métodos para acceder y modificar los atributos

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

    // CLASE INTERNA PARA MODELO VISITADO

    /**
     * Esta clase guarda la info básica del último modelo que visitó el cliente.
     * La API nos devuelve esto como un objeto dentro del JSON del cliente.
     * Solo tiene el ID, nombre y un link al modelo (formato HATEOAS).
     */
    public static class LastViewedModelInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        
        // ID del modelo
        private Long id;
        
        // Nombre del modelo (ej: "GPT-4", "Claude", etc.)
        private String nombre;
        
        // Link a la API para ver más detalles del modelo
        private String link;

        // Constructor vacío para que Jakarta pueda crear el objeto
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
