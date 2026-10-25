package edu.pe.cibertec.botica.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.cibertec.botica.dto.DetalleVentaRequest;
import edu.pe.cibertec.botica.dto.ProductoVendidoResponse;
import edu.pe.cibertec.botica.dto.ReporteVentasResponse;
import edu.pe.cibertec.botica.dto.UsuarioVentaResponse;
import edu.pe.cibertec.botica.dto.VentaRequest;
import edu.pe.cibertec.botica.entity.DetalleVenta;
import edu.pe.cibertec.botica.entity.Producto;
import edu.pe.cibertec.botica.entity.Venta;
import edu.pe.cibertec.botica.repository.ProductoRepository;
import edu.pe.cibertec.botica.repository.VentaRepository;

@RestController
@RequestMapping("/api/ventas")
public class VentasRestController {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentasRestController(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Venta> listar(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin) {
        if (inicio == null || fin == null) {
            return ventaRepository.findAll().stream()
                    .sorted(Comparator.comparing(Venta::getFecha).reversed())
                    .toList();
        }
        return ventaRepository.findByFechaBetweenOrderByFechaDesc(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscar(@PathVariable Long id) {
        return ventaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Venta> registrar(@RequestBody VentaRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto.");
        }

        Venta venta = new Venta();
        venta.setCodigo(generarCodigo());
        venta.setFecha(LocalDateTime.now());
        venta.setCliente(limpiarTexto(request.getCliente(), "Consumidor final"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        venta.setUsuario(authentication.getName());
        venta.setMetodoPago(limpiarTexto(request.getMetodoPago(), "Efectivo"));
        venta.setEstado("REGISTRADA");
        venta.setTotal(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVentaRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("El producto seleccionado no existe."));

            int cantidad = item.getCantidad() == null ? 0 : item.getCantidad();
            if (cantidad <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a cero.");
            }
            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new RuntimeException("El producto " + producto.getNombre() + " esta inactivo.");
            }
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + ".");
            }

            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            venta.getDetalles().add(detalle);
            total = total.add(subtotal);
        }

        venta.setTotal(total);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaRepository.save(venta));
    }

    @PutMapping("/{id}/anular")
    @Transactional
    public ResponseEntity<Venta> anular(@PathVariable Long id) {
        return ventaRepository.findById(id)
                .map(venta -> {
                    if (!"ANULADA".equals(venta.getEstado())) {
                        venta.setEstado("ANULADA");
                        for (DetalleVenta detalle : venta.getDetalles()) {
                            Producto producto = detalle.getProducto();
                            producto.setStock(producto.getStock() + detalle.getCantidad());
                            productoRepository.save(producto);
                        }
                    }
                    return ResponseEntity.ok(ventaRepository.save(venta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reporte")
    public ReporteVentasResponse reporte(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin) {
        LocalDate fechaInicio = inicio == null ? LocalDate.now().withDayOfMonth(1) : inicio;
        LocalDate fechaFin = fin == null ? LocalDate.now() : fin;
        List<Venta> ventas = ventaRepository.findByFechaBetweenOrderByFechaDesc(
                fechaInicio.atStartOfDay(),
                fechaFin.atTime(LocalTime.MAX));

        List<Venta> registradas = ventas.stream()
                .filter(venta -> "REGISTRADA".equals(venta.getEstado()))
                .toList();

        BigDecimal totalVendido = registradas.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long cantidadVentas = registradas.size();
        long ventasAnuladas = ventas.stream().filter(venta -> "ANULADA".equals(venta.getEstado())).count();
        BigDecimal promedio = cantidadVentas == 0
                ? BigDecimal.ZERO
                : totalVendido.divide(BigDecimal.valueOf(cantidadVentas), 2, RoundingMode.HALF_UP);

        List<ProductoVendidoResponse> productosMasVendidos = productosMasVendidos(registradas);
        List<UsuarioVentaResponse> ventasPorUsuario = ventasPorUsuario(registradas);

        return new ReporteVentasResponse(
                totalVendido,
                cantidadVentas,
                ventasAnuladas,
                promedio,
                productosMasVendidos,
                ventasPorUsuario);
    }

    private List<ProductoVendidoResponse> productosMasVendidos(List<Venta> ventas) {
        Map<String, ProductoAcumulado> acumulado = new LinkedHashMap<>();
        ventas.stream()
                .flatMap(venta -> venta.getDetalles().stream())
                .forEach(detalle -> {
                    String nombre = detalle.getProducto().getNombre();
                    ProductoAcumulado valor = acumulado.computeIfAbsent(nombre, key -> new ProductoAcumulado());
                    valor.cantidad += detalle.getCantidad();
                    valor.total = valor.total.add(detalle.getSubtotal());
                });

        return acumulado.entrySet().stream()
                .map(entry -> new ProductoVendidoResponse(entry.getKey(), entry.getValue().cantidad, entry.getValue().total))
                .sorted(Comparator.comparing(ProductoVendidoResponse::getCantidad).reversed())
                .limit(5)
                .toList();
    }

    private List<UsuarioVentaResponse> ventasPorUsuario(List<Venta> ventas) {
        return ventas.stream()
                .collect(Collectors.groupingBy(Venta::getUsuario))
                .entrySet().stream()
                .map(entry -> new UsuarioVentaResponse(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream().map(Venta::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add)))
                .sorted(Comparator.comparing(UsuarioVentaResponse::getTotal).reversed())
                .toList();
    }

    private String generarCodigo() {
        return "V" + String.format("%05d", ventaRepository.count() + 1);
    }

    private String limpiarTexto(String valor, String valorDefecto) {
        return valor == null || valor.isBlank() ? valorDefecto : valor.trim();
    }

    private static class ProductoAcumulado {
        private long cantidad;
        private BigDecimal total = BigDecimal.ZERO;
    }
}
