package edu.pe.cibertec.botica.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoRequest {

    private String codigo;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private LocalDate fechaVencimiento;
    private Boolean activo;
    private Long categoriaId;
    private Long proveedorId;
}
