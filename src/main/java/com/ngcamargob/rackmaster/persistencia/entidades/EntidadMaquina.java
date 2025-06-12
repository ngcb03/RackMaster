package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
@Entity
@Table(name="maquinas_virtuales")
public class EntidadMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maquina_id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 160, message = "El nombre debe tener un máximo de 160 caracteres.")
    private String nombre;

    @NotBlank(message = "El nombre en hipervisor es obligatorio.")
    @Size(max = 160, message = "El nombre en hipervisor debe tener un máximo de 160 caracteres.")
    private String nombre_en_hipervisor;

    @NotNull(message = "El ID en hipervisor es obligatorio.")
    private Integer id_en_hipervisor;

    @NotBlank(message = "La IP es obligatoria.")
//    @Pattern(regexp = "^\\d{1,3}(\\.\\d{1,3}){3}$", message = "La IP debe tener el formato correcto (ej. 192.168.1.1).")
    private String ip;

    @NotBlank(message = "La dirección MAC es obligatoria.")
//    @Pattern(
//            regexp = "^([0-9A-Fa-f]{2}([-:]?)){5}([0-9A-Fa-f]{2})$",
//            message = "La dirección MAC debe tener un formato válido (ej. AA:BB:CC:DD:EE:FF o AA-BB-CC-DD-EE-FF)."
//    )
    private String mac;

    @NotBlank(message = "El sistema operativo es obligatorio.")
    @Size(max = 160, message = "El sistema operativo debe tener un máximo de 160 caracteres.")
    private String sistema_op;

    @NotBlank(message = "El servicio es obligatorio.")
    @Size(max = 160, message = "El servicio debe tener un máximo de 160 caracteres.")
    private String servicio;

    @NotBlank(message = "El proyecto es obligatorio.")
    @Size(max = 160, message = "El proyecto debe tener un máximo de 160 caracteres.")
    private String proyecto;

    @NotBlank(message = "La aplicación es obligatoria.")
    @Size(max = 160, message = "La aplicación debe tener un máximo de 160 caracteres.")
    private String aplicacion;

    @NotBlank(message = "El procesador asignado es obligatorio.")
    @Size(max = 160, message = "El procesador asignado debe tener un máximo de 160 caracteres.")
    private String procesador_asig;

    @NotBlank(message = "El almacenamiento asignado es obligatorio.")
    @Size(max = 160, message = "El almacenamiento asignado debe tener un máximo de 160 caracteres.")
    private String almacenamiento_asig;

    @NotNull(message = "El almacenamiento total es obligatorio.")
    @Min(value = 0, message = "El almacenamiento total debe ser mayor o igual a 0.")
    private Integer almacenamiento_total;

    @NotNull(message = "La RAM asignada es obligatoria.")
    @Min(value = 0, message = "La RAM asignada debe ser mayor a 0.")
    private Integer ram_asig;

    private boolean en_uso;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "servidor_id",
            referencedColumnName = "servidor_id",
            nullable = false)
    private EntidadServidor servidor;

    @OneToMany(mappedBy = "maquina")
    private List<EntidadCredencial> credenciales;

    public EntidadMaquina() {
    }

    public EntidadMaquina(Integer maquina_id, String nombre, String nombre_en_hipervisor, Integer id_en_hipervisor, String ip, String mac, String sistema_op, String servicio, String proyecto, String aplicacion, String procesador_asig, String almacenamiento_asig, Integer almacenamiento_total, Integer ram_asig, boolean en_uso, EntidadServidor servidor, List<EntidadCredencial> credenciales) {
        this.maquina_id = maquina_id;
        this.nombre = nombre;
        this.nombre_en_hipervisor = nombre_en_hipervisor;
        this.id_en_hipervisor = id_en_hipervisor;
        this.ip = ip;
        this.mac = mac;
        this.sistema_op = sistema_op;
        this.servicio = servicio;
        this.proyecto = proyecto;
        this.aplicacion = aplicacion;
        this.procesador_asig = procesador_asig;
        this.almacenamiento_asig = almacenamiento_asig;
        this.almacenamiento_total = almacenamiento_total;
        this.ram_asig = ram_asig;
        this.en_uso = en_uso;
        this.servidor = servidor;
        this.credenciales = credenciales;
    }

    public Integer getMaquina_id() {
        return maquina_id;
    }

    public void setMaquina_id(Integer maquina_id) {
        this.maquina_id = maquina_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_en_hipervisor() {
        return nombre_en_hipervisor;
    }

    public void setNombre_en_hipervisor(String nombre_en_hipervisor) {
        this.nombre_en_hipervisor = nombre_en_hipervisor;
    }

    public Integer getId_en_hipervisor() {
        return id_en_hipervisor;
    }

    public void setId_en_hipervisor(Integer id_en_hipervisor) {
        this.id_en_hipervisor = id_en_hipervisor;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSistema_op() {
        return sistema_op;
    }

    public void setSistema_op(String sistema_op) {
        this.sistema_op = sistema_op;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getProcesador_asig() {
        return procesador_asig;
    }

    public void setProcesador_asig(String procesador_asig) {
        this.procesador_asig = procesador_asig;
    }

    public String getAlmacenamiento_asig() {
        return almacenamiento_asig;
    }

    public void setAlmacenamiento_asig(String almacenamiento_asig) {
        this.almacenamiento_asig = almacenamiento_asig;
    }

    public Integer getAlmacenamiento_total() {
        return almacenamiento_total;
    }

    public void setAlmacenamiento_total(Integer almacenamiento_total) {
        this.almacenamiento_total = almacenamiento_total;
    }

    public Integer getRam_asig() {
        return ram_asig;
    }

    public void setRam_asig(Integer ram_asig) {
        this.ram_asig = ram_asig;
    }

    public boolean isEn_uso() {
        return en_uso;
    }

    public void setEn_uso(boolean en_uso) {
        this.en_uso = en_uso;
    }

    public EntidadServidor getServidor() {
        return servidor;
    }

    public void setServidor(EntidadServidor servidor) {
        this.servidor = servidor;
    }

    public List<EntidadCredencial> getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(List<EntidadCredencial> credenciales) {
        this.credenciales = credenciales;
    }

    @Override
    public String toString() {
        return "EntidadMaquina{" +
                "maquina_id=" + maquina_id +
                ", nombre='" + nombre + '\'' +
                ", nombre_en_hipervisor='" + nombre_en_hipervisor + '\'' +
                ", id_en_hipervisor=" + id_en_hipervisor +
                ", ip='" + ip + '\'' +
                ", sistema_op='" + sistema_op + '\'' +
                ", servicio='" + servicio + '\'' +
                ", proyecto='" + proyecto + '\'' +
                ", aplicacion='" + aplicacion + '\'' +
                ", procesador_asig='" + procesador_asig + '\'' +
                ", almacenamiento_asig='" + almacenamiento_asig + '\'' +
                ", almacenamiento_total=" + almacenamiento_total +
                ", ram_asig=" + ram_asig +
                ", en_uso=" + en_uso +
                '}';
    }
}
