
package pe.edu.cibertec.producto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.cibertec.producto.entity.Producto;
import pe.edu.cibertec.producto.entity.CategoriaProducto;
import pe.edu.cibertec.producto.entity.Proveedor;

import pe.edu.cibertec.producto.repository.ProductoRepository;
import pe.edu.cibertec.producto.repository.CategoriaRepository;
import pe.edu.cibertec.producto.repository.ProveedorRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            ProveedorRepository proveedorRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository
                .findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Producto guardar(Producto producto) {

        if (producto.getCategoria() == null
                || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una categoría.");
        }

        if (producto.getProveedor() == null
                || producto.getProveedor().getId() == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar un proveedor.");
        }

        Long categoriaId = producto.getCategoria().getId();
        Long proveedorId = producto.getProveedor().getId();

        CategoriaProducto categoria =
                categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La categoría seleccionada no existe."));

        Proveedor proveedor =
                proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El proveedor seleccionado no existe."));

        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);

        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

@Transactional
public Producto guardarDesdeRequest(
        pe.edu.cibertec.producto.dto.ProductoRequest request) {

    CategoriaProducto categoria =
            categoriaRepository.findById(request.categoriaId())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "La categoría seleccionada no existe."));

    Proveedor proveedor =
            proveedorRepository.findById(request.proveedorId())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "El proveedor seleccionado no existe."));

    Producto producto = new Producto();

    producto.setCodigo(request.codigo());
    producto.setNombre(request.nombre());
    producto.setPrecio(request.precio());
    producto.setStock(request.stock());
    producto.setFechaVencimiento(request.fechaVencimiento());
    producto.setActivo(request.activo());
    producto.setCategoria(categoria);
    producto.setProveedor(proveedor);

    return productoRepository.save(producto);
}

@Transactional
public Producto actualizarDesdeRequest(
        Long id,
        pe.edu.cibertec.producto.dto.ProductoRequest request) {

    Producto producto = productoRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "El producto no existe."));

    CategoriaProducto categoria =
            categoriaRepository.findById(request.categoriaId())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "La categoría seleccionada no existe."));

    Proveedor proveedor =
            proveedorRepository.findById(request.proveedorId())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "El proveedor seleccionado no existe."));

    producto.setCodigo(request.codigo());
    producto.setNombre(request.nombre());
    producto.setPrecio(request.precio());
    producto.setStock(request.stock());
    producto.setFechaVencimiento(request.fechaVencimiento());
    producto.setActivo(request.activo());
    producto.setCategoria(categoria);
    producto.setProveedor(proveedor);

    return productoRepository.save(producto);
}

}
