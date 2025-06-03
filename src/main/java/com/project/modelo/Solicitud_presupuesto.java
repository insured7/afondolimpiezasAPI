package com.project.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad de la solicitud del presupuesto enviada por un usuario en los
 * formularios
 */

@Entity
@Table(name = "solicitudes_presupuesto", schema = "core")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud_presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitudes_presupuesto")
    private Long id_solicitudes_presupuesto;

    private String detalles;

    private String estado;
    
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @JsonProperty("usuarioId")
    public Long getUsuarioId() {
        return (usuario != null) ? usuario.getId_usuario() : null;
    }
}

