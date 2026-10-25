package edu.pe.cibertec.botica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleVentaRequest {

    private Long productoId;
    private Integer cantidad;
}
