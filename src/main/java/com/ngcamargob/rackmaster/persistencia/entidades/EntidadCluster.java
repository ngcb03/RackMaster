package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="clusters")
public class EntidadCluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cluster_id;

    @NotBlank(message = "El nombre del clúster es obligatorio.")
    @Size(max = 160, message = "El nombre debe tener un máximo de 160 caracteres.")
    private String nombre;

    @Size(max = 160, message = "El proyecto debe tener un máximo de 160 caracteres.")
    private String proyecto;

    @NotBlank(message = "El nombre de la sede es obligatorio.")
    @Size(max = 160, message = "El nombre de la sede debe tener un máximo de 160 caracteres.")
    private String sede;

    @OneToMany(mappedBy = "cluster")
    private List<EntidadServidor> servidores;

    @Override
    public String toString() {
        return "EntidadCluster{" +
                "cluster_id=" + cluster_id +
                ", nombre='" + nombre + '\'' +
                ", proyecto='" + proyecto + '\'' +
                ", sede='" + sede + '\'' +
                ", servidores=" + servidores +
                '}';
    }
}
