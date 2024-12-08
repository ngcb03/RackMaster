package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
@Entity
@Table(name="servidores_fisicos")
public class EntidadServidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer servidor_id;

    private boolean en_uso;

    @NotBlank
    @Size(max = 160, message = "El nombre debe tener un máximo de 160 caracteres.")
    private String nombre;

    @NotBlank(message = "La IP es obligatoria.")
//    @Pattern(
//            regexp = "^\\d{1,3}(\\.\\d{1,3}){3}$",
//            message = "La IP debe tener el formato correcto (ej. 192.168.1.1).")
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

    @NotBlank(message = "El modelo es obligatorio.")
    @Size(max = 160, message = "El modelo debe tener un máximo de 120 caracteres.")
    private String modelo;

    @NotBlank(message = "El procesador es obligatorio.")
    @Size(max = 160, message = "El procesador debe tener un máximo de 160 caracteres.")
    private String procesador;

    @NotBlank(message = "El campo de discos no puede estar vacío.")
    @Size(max = 160, message = "Los discos deben tener un máximo de 160 caracteres.")
    private String discos;

    @NotNull(message = "El almacenamiento total es obligatorio.")
    @Min(value = 0, message = "El almacenamiento total debe ser mayor a 0.")
    private Integer almacenamiento_total;

    @NotNull(message = "La memoria RAM es obligatoria.")
    @Min(value = 0, message = "La memoria RAM debe ser mayor o igual a 0.")
    private Integer ram;

    @NotBlank(message = "El número de serie es obligatorio.")
    @Size(max = 160, message = "El número de serie debe tener un máximo de 160 caracteres.")
    private String serial;

    @NotNull(message = "La placa es obligatoria.")
    private Integer placa;

    @NotBlank(message = "El rack es obligatorio.")
    @Size(max = 160, message = "El rack debe tener un máximo de 160 caracteres.")
    private String rack;

    @Min(value = 0, message = "La unidad debe ser mayor o igual a 0.")
    @Max(value = 43, message = "La unidad debe ser menor o igual a 50.")
    private byte unidad;

    @NotBlank(message = "El nombre de la sede es obligatorio.")
    @Size(max = 160, message = "El nombre de la sede debe tener un máximo de 160 caracteres.")
    private String sede;

    @OneToMany(mappedBy = "servidor")
    private List<EntidadMaquina> maquinas;

    @OneToMany(mappedBy = "servidor")
    private List<EntidadCredencial> credenciales;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id")
    private EntidadCluster cluster;

    public EntidadServidor() {
    }

    public EntidadServidor(Integer servidor_id, boolean en_uso, String nombre, String ip, String mac, String sistema_op, String modelo, String procesador, String discos, Integer almacenamiento_total, Integer ram, String serial, Integer placa, String rack, byte unidad, String sede, List<EntidadMaquina> maquinas, List<EntidadCredencial> credenciales, EntidadCluster cluster) {
        this.servidor_id = servidor_id;
        this.en_uso = en_uso;
        this.nombre = nombre;
        this.ip = ip;
        this.mac = mac;
        this.sistema_op = sistema_op;
        this.modelo = modelo;
        this.procesador = procesador;
        this.discos = discos;
        this.almacenamiento_total = almacenamiento_total;
        this.ram = ram;
        this.serial = serial;
        this.placa = placa;
        this.rack = rack;
        this.unidad = unidad;
        this.sede = sede;
        this.maquinas = maquinas;
        this.credenciales = credenciales;
        this.cluster = cluster;
    }

    public Integer getServidor_id() {
        return servidor_id;
    }

    public void setServidor_id(Integer servidor_id) {
        this.servidor_id = servidor_id;
    }

    public boolean isEn_uso() {
        return en_uso;
    }

    public void setEn_uso(boolean en_uso) {
        this.en_uso = en_uso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public String getDiscos() {
        return discos;
    }

    public void setDiscos(String discos) {
        this.discos = discos;
    }

    public Integer getAlmacenamiento_total() {
        return almacenamiento_total;
    }

    public void setAlmacenamiento_total(Integer almacenamiento_total) {
        this.almacenamiento_total = almacenamiento_total;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getPlaca() {
        return placa;
    }

    public void setPlaca(Integer placa) {
        this.placa = placa;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public byte getUnidad() {
        return unidad;
    }

    public void setUnidad(byte unidad) {
        this.unidad = unidad;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public List<EntidadMaquina> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(List<EntidadMaquina> maquinas) {
        this.maquinas = maquinas;
    }

    public List<EntidadCredencial> getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(List<EntidadCredencial> credenciales) {
        this.credenciales = credenciales;
    }

    public EntidadCluster getCluster() {
        return cluster;
    }

    public void setCluster(EntidadCluster cluster) {
        this.cluster = cluster;
    }


    @Override
    public String toString() {
        return "EntidadServidor{" +
                "servidor_id=" + servidor_id +
                ", nombre='" + nombre + '\'' +
                ", ip='" + ip + '\'' +
                ", sistema_op='" + sistema_op + '\'' +
                ", modelo='" + modelo + '\'' +
                ", procesador='" + procesador + '\'' +
                ", discos='" + discos + '\'' +
                ", almacenamiento_total=" + almacenamiento_total +
                ", ram=" + ram +
                ", serial='" + serial + '\'' +
                ", placa=" + placa +
                ", rack='" + rack + '\'' +
                ", unidad=" + unidad +
                ", en_uso=" + en_uso +
                '}';
    }
}
