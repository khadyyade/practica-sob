package deim.urv.cat.homework2.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object para representar un comentario.
 * 
 * RESPONSABILIDADES:
 * - Representar la estructura JSON/XML de comentarios devuelta por la API REST
 * - NO es una entidad JPA, solo un POJO para transferencia de datos
 * 
 * ASIGNADO A: Persona A
 */
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String text;
    private Date createdAt;
    private String customerName; // Nombre del cliente que hizo el comentario

    // ==================== CONSTRUCTORES ====================

    public CommentDTO() {
    }

    public CommentDTO(Long id, String text, Date createdAt, String customerName) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.customerName = customerName;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
