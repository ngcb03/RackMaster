package com.ngcamargob.rackmaster.utilidades.mapper;

import java.util.List;

public interface IMapper <I,O>{
    O getIO(I in);
    I getOI(O on);
    List<O> getLIO(List<I> li);
}
