package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

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

    public EntidadCluster() {
    }

    public EntidadCluster(Integer cluster_id, String nombre, String proyecto, String sede, List<EntidadServidor> servidores) {
        this.cluster_id = cluster_id;
        this.nombre = nombre;
        this.proyecto = proyecto;
        this.sede = sede;
        this.servidores = servidores;
    }

    public Integer getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(Integer cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public List<EntidadServidor> getServidores() {
        return servidores;
    }

    public void setServidores(List<EntidadServidor> servidores) {
        this.servidores = servidores;
    }

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
