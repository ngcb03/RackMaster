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
@Table(name="servidores_fisicos")
public class EntidadServidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer servidor_id;

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

    @Column(name = "en_uso")
    private boolean en_uso;

    @OneToMany(mappedBy = "servidor")
    private List<EntidadMaquina> maquinas;

    @OneToMany(mappedBy = "servidor")
    private List<EntidadCredencial> credenciales;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id")
    private EntidadCluster cluster;


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
