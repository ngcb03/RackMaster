package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "credenciales")
public class EntidadCredencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer credencial_id;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 160, message = "El nombre de usuario debe tener un máximo de 160 caracteres.")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 160, message = "La contraseña debe tener un máximo de 160 caracteres.")
    private String contrasenia;

    @NotNull(message = "El puerto es obligatorio.")
    @Min(value = 1, message = "El puerto debe ser un valor positivo.")
    @Max(value = 1000000, message = "El puerto debe ser un número válido entre 1 y 1000000.")
    private Integer puerto;

    @NotBlank(message = "El tipo de conexión es obligatorio.")
    @Size(max = 80, message = "El tipo de conexión debe tener un máximo de 80 caracteres.")
    private String tipo_conexion;

    @Size(max = 160, message = "Los privilegios deben tener un máximo de 160 caracteres.")
    private String privilegios;

    @Size(max = 160, message = "El uso destinado debe tener un máximo de 160 caracteres.")
    private String uso_destinado;

    @Column(name = "primaria")
    private boolean primaria;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "servidor_id", referencedColumnName = "servidor_id")
    private EntidadServidor servidor;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "maquina_id", referencedColumnName = "maquina_id")
    private EntidadMaquina maquina;

    @Override
    public String toString() {
        return "EntidadCredencial{" +
                "credencial_id=" + credencial_id +
                ", usuario='" + usuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", puerto=" + puerto +
                ", tipo_conexion='" + tipo_conexion + '\'' +
                ", privilegios='" + privilegios + '\'' +
                ", uso_destinado='" + uso_destinado + '\'' +
                ", primaria=" + primaria +
                ", servidor=" + servidor +
                ", maquina=" + maquina +
                '}';
    }

}
