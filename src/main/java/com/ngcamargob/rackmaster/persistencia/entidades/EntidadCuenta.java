package com.ngcamargob.rackmaster.persistencia.entidades;

import com.ngcamargob.rackmaster.persistencia.entidades.enums_entity.EntidadCargo;
import com.ngcamargob.rackmaster.persistencia.entidades.enums_entity.EntidadRol;
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
@Table(name="cuentas")
public class EntidadCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cuenta_id;

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(max = 160, message = "El nombre completo debe tener un máximo de 160 caracteres.")
    private String nombre_completo;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 160, message = "El nombre de usuario debe tener un máximo de 160 caracteres.")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 260, message = "La contraseña debe tener entre 8 y 260 caracteres.")
    private String contrasenia;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id", referencedColumnName = "cargo_id", nullable = false)
    private EntidadCargo entidadCargo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", referencedColumnName = "rol_id", nullable = false)
    private EntidadRol entidadRol;

    @Column(name = "habilitado")
    private boolean habilitado;

    @Override
    public String toString() {
        return "EntidadCuenta{" +
                "cuenta_id=" + cuenta_id +
                ", nombre_completo='" + nombre_completo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", entidadCargo=" + entidadCargo +
                ", entidadRol=" + entidadRol +
                ", habilitado=" + habilitado +
                '}';
    }
}
