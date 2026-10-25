
package pe.edu.cibertec.producto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductoRequest(
        String codigo,
        String nombre,
        BigDecimal precio,
        Integer stock,
        LocalDate fechaVencimiento,
        Boolean activo,
        Long categoriaId,
        Long proveedorId
) {
}
