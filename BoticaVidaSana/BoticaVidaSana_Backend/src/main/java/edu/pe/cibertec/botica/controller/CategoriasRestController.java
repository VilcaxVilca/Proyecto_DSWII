package edu.pe.cibertec.botica.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.cibertec.botica.entity.CategoriaProducto;
import edu.pe.cibertec.botica.repository.CategoriaProductoRepository;

@RestController
@RequestMapping("/api/categorias")
public class CategoriasRestController {

    private final CategoriaProductoRepository repository;

    public CategoriasRestController(CategoriaProductoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CategoriaProducto> listar() {
        return repository.findAll();
    }
}
