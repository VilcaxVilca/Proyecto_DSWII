package edu.pe.cibertec.botica.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioVentaResponse {

    private String usuario;
    private long cantidadVentas;
    private BigDecimal total;
}
