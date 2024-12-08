package com.ngcamargob.rackmaster.utilidades.mapper;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCargo;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCuenta;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadRol;
import com.ngcamargob.rackmaster.persistencia.entidades.enums.ECargo;
import com.ngcamargob.rackmaster.persistencia.entidades.enums.ERol;
import com.ngcamargob.rackmaster.presentacion.dto.CuentaDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvCuentaDTO implements IMapper <EntidadCuenta, CuentaDTO>{

    @Override
    public CuentaDTO getIO(EntidadCuenta entidadCuenta) {
        CuentaDTO cuentaDTO = new CuentaDTO();

        if(entidadCuenta != null) {
            cuentaDTO = CuentaDTO.builder()
                    .cuenta_id(entidadCuenta.getCuenta_id())
                    .nombre_completo(entidadCuenta.getNombre_completo())
                    .usuario(entidadCuenta.getUsuario())
                    .contrasenia(entidadCuenta.getContrasenia())
                    .cargo(
                            entidadCuenta.getEntidadCargo().getCargoUsuario().toString()
                    )
                    .rol(
                            entidadCuenta.getEntidadRol().getRolUsuario().toString()
                    )
                    .habilitado(entidadCuenta.isHabilitado())
                    .build();
        } return cuentaDTO;
    }

    @Override
    public EntidadCuenta getOI(CuentaDTO cuentaDTO) {
        EntidadCuenta entidadCuenta = new EntidadCuenta();

        if(cuentaDTO != null) {
            EntidadCargo entidadCargo = new EntidadCargo();
            switch (cuentaDTO.getCargo()) {
                case "JEFE":
                case "LIDER":
                case "ANALISTA":
                case "OPERADOR":
                case "OTRO":
                    entidadCargo.setCargoUsuario(ECargo.valueOf(cuentaDTO.getCargo()));
                    break;
                default: entidadCargo.setCargoUsuario(ECargo.valueOf("OTRO"));
            };

            EntidadRol entidadRol = new EntidadRol();
            switch (cuentaDTO.getRol()) {
                case "SUPER_ADMIN":
                case "ADMIN":
                case "USER":
                    entidadRol.setRolUsuario(ERol.valueOf(cuentaDTO.getRol()));
                    break;
            };

            entidadCuenta = EntidadCuenta.builder()
                    .cuenta_id(cuentaDTO.getCuenta_id())
                    .nombre_completo(cuentaDTO.getNombre_completo())
                    .usuario(cuentaDTO.getUsuario())
                    .contrasenia(cuentaDTO.getContrasenia())
                    .entidadCargo(entidadCargo)
                    .entidadRol(entidadRol)
                    .habilitado(cuentaDTO.isHabilitado())
                    .build();
        } return entidadCuenta;
    }

    @Override
    public List<CuentaDTO> getLIO(List<EntidadCuenta> cuentas) {
        return cuentas.stream().map(this::getIO).collect(Collectors.toList());
    }

}
