package edu.pe.cibertec.botica.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaRequest {

    private String cliente;
    private String usuario;
    private String metodoPago;
    private List<DetalleVentaRequest> detalles;
}
