package edu.pe.cibertec.botica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.cibertec.botica.dto.ProductoRequest;
import edu.pe.cibertec.botica.entity.CategoriaProducto;
import edu.pe.cibertec.botica.entity.Producto;
import edu.pe.cibertec.botica.entity.Proveedor;
import edu.pe.cibertec.botica.repository.CategoriaProductoRepository;
import edu.pe.cibertec.botica.repository.ProductoRepository;
import edu.pe.cibertec.botica.repository.ProveedorRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductosRestController {

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductosRestController(
            ProductoRepository productoRepository,
            CategoriaProductoRepository categoriaRepository,
            ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @GetMapping
    public List<Producto> listar(@RequestParam(required = false) String texto) {
        if (texto == null || texto.isBlank()) {
            return productoRepository.findAll();
        }
        return productoRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(texto, texto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> registrar(@RequestBody ProductoRequest request) {
        Producto producto = new Producto();
        completarProducto(producto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoRepository.save(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody ProductoRequest request) {
        return productoRepository.findById(id)
                .map(producto -> {
                    completarProducto(producto, request);
                    return ResponseEntity.ok(productoRepository.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void completarProducto(Producto producto, ProductoRequest request) {
        CategoriaProducto categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("La categoria seleccionada no existe."));
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new RuntimeException("El proveedor seleccionado no existe."));

        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setFechaVencimiento(request.getFechaVencimiento());
        producto.setActivo(request.getActivo() == null ? true : request.getActivo());
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
    }
}
