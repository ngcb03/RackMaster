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

    @Column(name = "en_uso")
    private boolean en_uso;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "servidor_id",
            referencedColumnName = "servidor_id",
            nullable = false)
    private EntidadServidor servidor;

    @OneToMany(mappedBy = "maquina")
    private List<EntidadCredencial> credenciales;

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
