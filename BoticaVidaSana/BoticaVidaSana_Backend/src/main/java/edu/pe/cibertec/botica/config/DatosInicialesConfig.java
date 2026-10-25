package edu.pe.cibertec.botica.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.pe.cibertec.botica.entity.CategoriaProducto;
import edu.pe.cibertec.botica.entity.Producto;
import edu.pe.cibertec.botica.entity.Proveedor;
import edu.pe.cibertec.botica.entity.Usuario;
import edu.pe.cibertec.botica.entity.DetalleVenta;
import edu.pe.cibertec.botica.entity.Venta;
import edu.pe.cibertec.botica.repository.CategoriaProductoRepository;
import edu.pe.cibertec.botica.repository.ProductoRepository;
import edu.pe.cibertec.botica.repository.ProveedorRepository;
import edu.pe.cibertec.botica.repository.UsuarioRepository;
import edu.pe.cibertec.botica.repository.VentaRepository;

@Configuration
public class DatosInicialesConfig {

    @Bean
    CommandLineRunner cargarDatosIniciales(
            CategoriaProductoRepository categoriaRepository,
            ProveedorRepository proveedorRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            VentaRepository ventaRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            usuario(usuarioRepository, passwordEncoder, "admin", "admin123", "ADMINISTRADOR");
            usuario(usuarioRepository, passwordEncoder, "farmaceutico", "farma123", "FARMACEUTICO");
            usuario(usuarioRepository, passwordEncoder, "cajero", "cajero123", "CAJERO");

            Map<String, CategoriaProducto> categorias = Map.ofEntries(
                    Map.entry("Analgesicos", categoria(categoriaRepository, "Analgesicos", "Dolor, fiebre e inflamacion.")),
                    Map.entry("Antibioticos", categoria(categoriaRepository, "Antibioticos", "Medicamentos sujetos a receta medica.")),
                    Map.entry("Vitaminas", categoria(categoriaRepository, "Vitaminas", "Suplementos y cuidado preventivo.")),
                    Map.entry("Respiratorio", categoria(categoriaRepository, "Respiratorio", "Tos, gripe, congestion y alergias.")),
                    Map.entry("Digestivo", categoria(categoriaRepository, "Digestivo", "Acidez, diarrea, gases y digestion.")),
                    Map.entry("Dermatologia", categoria(categoriaRepository, "Dermatologia", "Cuidado de piel, heridas y tratamientos topicos.")),
                    Map.entry("Higiene", categoria(categoriaRepository, "Higiene", "Aseo personal y limpieza sanitaria.")),
                    Map.entry("Botiquin", categoria(categoriaRepository, "Botiquin", "Primeros auxilios y material medico.")),
                    Map.entry("Infantil", categoria(categoriaRepository, "Infantil", "Productos para bebes y ninos.")));

            Map<String, Proveedor> proveedores = Map.ofEntries(
                    Map.entry("20601234567", proveedor(proveedorRepository, "20601234567", "Distribuidora Salud Peru SAC", "ventas@saludperu.com", "987654321")),
                    Map.entry("20555666771", proveedor(proveedorRepository, "20555666771", "FarmaAndina Distribuciones", "contacto@farmaandina.pe", "965112233")),
                    Map.entry("20444555662", proveedor(proveedorRepository, "20444555662", "Laboratorios Vida Farma", "pedidos@vidafarma.pe", "944778899")),
                    Map.entry("20333444553", proveedor(proveedorRepository, "20333444553", "Medisur Importaciones", "ventas@medisur.pe", "933224455")));

            producto(productoRepository, "MED001", "Paracetamol 500 mg", "4.50", 80, LocalDate.now().plusYears(2), true, categorias.get("Analgesicos"), proveedores.get("20601234567"));
            producto(productoRepository, "MED002", "Ibuprofeno 400 mg", "6.90", 45, LocalDate.now().plusMonths(18), true, categorias.get("Analgesicos"), proveedores.get("20601234567"));
            producto(productoRepository, "MED003", "Naproxeno 550 mg", "8.80", 9, LocalDate.now().plusMonths(10), true, categorias.get("Analgesicos"), proveedores.get("20555666771"));
            producto(productoRepository, "MED004", "Diclofenaco gel 1%", "12.90", 18, LocalDate.now().plusMonths(14), true, categorias.get("Analgesicos"), proveedores.get("20444555662"));

            producto(productoRepository, "ATB001", "Amoxicilina 500 mg", "15.90", 24, LocalDate.now().plusMonths(11), true, categorias.get("Antibioticos"), proveedores.get("20444555662"));
            producto(productoRepository, "ATB002", "Azitromicina 500 mg", "22.50", 12, LocalDate.now().plusMonths(9), true, categorias.get("Antibioticos"), proveedores.get("20555666771"));
            producto(productoRepository, "ATB003", "Cefalexina 500 mg", "18.40", 7, LocalDate.now().plusMonths(7), true, categorias.get("Antibioticos"), proveedores.get("20333444553"));

            producto(productoRepository, "VIT001", "Vitamina C 1000 mg", "12.50", 35, LocalDate.now().plusYears(2), true, categorias.get("Vitaminas"), proveedores.get("20601234567"));
            producto(productoRepository, "VIT002", "Complejo B x 30 tabletas", "19.90", 28, LocalDate.now().plusMonths(20), true, categorias.get("Vitaminas"), proveedores.get("20555666771"));
            producto(productoRepository, "VIT003", "Multivitaminico adulto", "32.90", 14, LocalDate.now().plusMonths(16), true, categorias.get("Vitaminas"), proveedores.get("20444555662"));
            producto(productoRepository, "VIT004", "Vitamina D3 2000 UI", "27.50", 5, LocalDate.now().plusMonths(12), true, categorias.get("Vitaminas"), proveedores.get("20333444553"));

            producto(productoRepository, "RES001", "Loratadina 10 mg", "9.50", 40, LocalDate.now().plusMonths(15), true, categorias.get("Respiratorio"), proveedores.get("20601234567"));
            producto(productoRepository, "RES002", "Jarabe para la tos adulto", "18.90", 10, LocalDate.now().plusDays(50), true, categorias.get("Respiratorio"), proveedores.get("20555666771"));
            producto(productoRepository, "RES003", "Descongestionante nasal spray", "16.90", 16, LocalDate.now().plusMonths(8), true, categorias.get("Respiratorio"), proveedores.get("20333444553"));
            producto(productoRepository, "RES004", "Pastillas para garganta miel limon", "7.90", 32, LocalDate.now().plusMonths(13), true, categorias.get("Respiratorio"), proveedores.get("20444555662"));

            producto(productoRepository, "DIG001", "Omeprazol 20 mg", "11.90", 38, LocalDate.now().plusMonths(19), true, categorias.get("Digestivo"), proveedores.get("20444555662"));
            producto(productoRepository, "DIG002", "Sales de rehidratacion oral", "3.50", 60, LocalDate.now().plusMonths(24), true, categorias.get("Digestivo"), proveedores.get("20601234567"));
            producto(productoRepository, "DIG003", "Simeticona gotas", "13.90", 8, LocalDate.now().plusDays(35), true, categorias.get("Digestivo"), proveedores.get("20555666771"));
            producto(productoRepository, "DIG004", "Antiacido masticable", "6.50", 4, LocalDate.now().plusMonths(6), true, categorias.get("Digestivo"), proveedores.get("20333444553"));

            producto(productoRepository, "DER001", "Clotrimazol crema 1%", "14.80", 20, LocalDate.now().plusMonths(17), true, categorias.get("Dermatologia"), proveedores.get("20333444553"));
            producto(productoRepository, "DER002", "Bloqueador solar FPS 50", "39.90", 25, LocalDate.now().plusMonths(22), true, categorias.get("Dermatologia"), proveedores.get("20555666771"));
            producto(productoRepository, "DER003", "Alcohol medicinal 70%", "8.50", 55, LocalDate.now().plusMonths(30), true, categorias.get("Dermatologia"), proveedores.get("20601234567"));
            producto(productoRepository, "DER004", "Agua oxigenada 120 ml", "5.80", 6, LocalDate.now().plusDays(28), true, categorias.get("Dermatologia"), proveedores.get("20444555662"));

            producto(productoRepository, "HIG001", "Gel antibacterial 250 ml", "9.90", 50, LocalDate.now().plusYears(2), true, categorias.get("Higiene"), proveedores.get("20601234567"));
            producto(productoRepository, "HIG002", "Jabon antiseptico", "7.50", 30, LocalDate.now().plusMonths(18), true, categorias.get("Higiene"), proveedores.get("20555666771"));
            producto(productoRepository, "HIG003", "Mascarillas quirurgicas x 50", "18.00", 22, LocalDate.now().plusYears(3), true, categorias.get("Higiene"), proveedores.get("20333444553"));
            producto(productoRepository, "HIG004", "Papel higienico humedo", "10.90", 3, LocalDate.now().plusMonths(14), true, categorias.get("Higiene"), proveedores.get("20444555662"));

            producto(productoRepository, "BOT001", "Gasas esteriles x 10", "6.20", 44, LocalDate.now().plusMonths(25), true, categorias.get("Botiquin"), proveedores.get("20601234567"));
            producto(productoRepository, "BOT002", "Vendas elasticas 4 pulgadas", "8.70", 11, LocalDate.now().plusYears(3), true, categorias.get("Botiquin"), proveedores.get("20555666771"));
            producto(productoRepository, "BOT003", "Curitas surtidas x 100", "12.40", 2, LocalDate.now().plusMonths(21), true, categorias.get("Botiquin"), proveedores.get("20333444553"));
            producto(productoRepository, "BOT004", "Termometro digital", "24.90", 17, LocalDate.now().plusYears(4), true, categorias.get("Botiquin"), proveedores.get("20444555662"));

            producto(productoRepository, "INF001", "Panal infantil talla M", "36.90", 15, LocalDate.now().plusYears(2), true, categorias.get("Infantil"), proveedores.get("20555666771"));
            producto(productoRepository, "INF002", "Jarabe pediatrico multivitaminico", "21.90", 6, LocalDate.now().plusDays(45), true, categorias.get("Infantil"), proveedores.get("20444555662"));
            producto(productoRepository, "INF003", "Crema para escaldaduras", "17.60", 19, LocalDate.now().plusMonths(16), true, categorias.get("Infantil"), proveedores.get("20333444553"));
            producto(productoRepository, "INF004", "Toallitas humedas bebe", "11.80", 26, LocalDate.now().plusMonths(18), true, categorias.get("Infantil"), proveedores.get("20601234567"));

            ventaInicial(ventaRepository, productoRepository, "V00001", LocalDateTime.now().minusDays(2), "Consumidor final", "cajero", "Efectivo",
                    Map.of("MED001", 2, "VIT001", 1, "BOT001", 2));
            ventaInicial(ventaRepository, productoRepository, "V00002", LocalDateTime.now().minusDays(1), "Maria Torres", "farmaceutico", "Yape",
                    Map.of("RES002", 1, "DIG002", 3));
            ventaInicial(ventaRepository, productoRepository, "V00003", LocalDateTime.now().minusHours(5), "Consumidor final", "cajero", "Tarjeta",
                    Map.of("DER003", 1, "HIG001", 2, "BOT003", 1));
        };
    }

    private Usuario usuario(UsuarioRepository repository, PasswordEncoder passwordEncoder, String username, String password, String rol) {
        Usuario usuario = repository.findByUsername(username);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setUsername(username);
        }
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuario.setEstado(true);
        return repository.save(usuario);
    }

    private CategoriaProducto categoria(CategoriaProductoRepository repository, String nombre, String descripcion) {
        return repository.findByNombre(nombre).orElseGet(() -> {
            CategoriaProducto categoria = new CategoriaProducto();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            return repository.save(categoria);
        });
    }

    private Proveedor proveedor(ProveedorRepository repository, String ruc, String razonSocial, String correo, String telefono) {
        return repository.findByRuc(ruc).orElseGet(() -> {
            Proveedor proveedor = new Proveedor();
            proveedor.setRuc(ruc);
            proveedor.setRazonSocial(razonSocial);
            proveedor.setCorreo(correo);
            proveedor.setTelefono(telefono);
            return repository.save(proveedor);
        });
    }

    private void producto(
            ProductoRepository repository,
            String codigo,
            String nombre,
            String precio,
            Integer stock,
            LocalDate fechaVencimiento,
            Boolean activo,
            CategoriaProducto categoria,
            Proveedor proveedor) {
        if (repository.findByCodigo(codigo).isPresent()) {
            return;
        }

        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setStock(stock);
        producto.setFechaVencimiento(fechaVencimiento);
        producto.setActivo(activo);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        repository.save(producto);
    }

    private void ventaInicial(
            VentaRepository ventaRepository,
            ProductoRepository productoRepository,
            String codigo,
            LocalDateTime fecha,
            String cliente,
            String usuario,
            String metodoPago,
            Map<String, Integer> items) {
        if (ventaRepository.findByCodigo(codigo).isPresent()) {
            return;
        }

        Venta venta = new Venta();
        venta.setCodigo(codigo);
        venta.setFecha(fecha);
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta.setMetodoPago(metodoPago);
        venta.setEstado("REGISTRADA");
        venta.setTotal(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            Producto producto = productoRepository.findByCodigo(item.getKey()).orElseThrow();
            Integer cantidad = item.getValue();
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
        ventaRepository.save(venta);
    }
}
