package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;

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

    private boolean primaria;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "servidor_id", referencedColumnName = "servidor_id")
    private EntidadServidor servidor;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "maquina_id", referencedColumnName = "maquina_id")
    private EntidadMaquina maquina;

    public EntidadCredencial() {
    }

    public EntidadCredencial(Integer credencial_id, String usuario, String contrasenia, Integer puerto, String tipo_conexion, String privilegios, String uso_destinado, boolean primaria, EntidadServidor servidor, EntidadMaquina maquina) {
        this.credencial_id = credencial_id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.puerto = puerto;
        this.tipo_conexion = tipo_conexion;
        this.privilegios = privilegios;
        this.uso_destinado = uso_destinado;
        this.primaria = primaria;
        this.servidor = servidor;
        this.maquina = maquina;
    }

    public Integer getCredencial_id() {
        return credencial_id;
    }

    public void setCredencial_id(Integer credencial_id) {
        this.credencial_id = credencial_id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public String getTipo_conexion() {
        return tipo_conexion;
    }

    public void setTipo_conexion(String tipo_conexion) {
        this.tipo_conexion = tipo_conexion;
    }

    public String getPrivilegios() {
        return privilegios;
    }

    public void setPrivilegios(String privilegios) {
        this.privilegios = privilegios;
    }

    public String getUso_destinado() {
        return uso_destinado;
    }

    public void setUso_destinado(String uso_destinado) {
        this.uso_destinado = uso_destinado;
    }

    public boolean isPrimaria() {
        return primaria;
    }

    public void setPrimaria(boolean primaria) {
        this.primaria = primaria;
    }

    public EntidadServidor getServidor() {
        return servidor;
    }

    public void setServidor(EntidadServidor servidor) {
        this.servidor = servidor;
    }

    public EntidadMaquina getMaquina() {
        return maquina;
    }

    public void setMaquina(EntidadMaquina maquina) {
        this.maquina = maquina;
    }

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
