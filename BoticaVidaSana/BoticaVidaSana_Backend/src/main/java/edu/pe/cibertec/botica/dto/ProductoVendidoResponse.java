package edu.pe.cibertec.botica.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductoVendidoResponse {

    private String producto;
    private long cantidad;
    private BigDecimal total;
}
