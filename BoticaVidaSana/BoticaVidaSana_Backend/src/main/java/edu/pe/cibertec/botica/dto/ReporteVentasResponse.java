package edu.pe.cibertec.botica.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReporteVentasResponse {

    private BigDecimal totalVendido;
    private long cantidadVentas;
    private long ventasAnuladas;
    private BigDecimal promedioVenta;
    private List<ProductoVendidoResponse> productosMasVendidos;
    private List<UsuarioVentaResponse> ventasPorUsuario;
}
