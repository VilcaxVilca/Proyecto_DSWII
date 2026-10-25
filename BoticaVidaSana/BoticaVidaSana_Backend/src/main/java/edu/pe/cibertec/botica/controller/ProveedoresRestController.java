package edu.pe.cibertec.botica.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.cibertec.botica.entity.Proveedor;
import edu.pe.cibertec.botica.repository.ProveedorRepository;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedoresRestController {

    private final ProveedorRepository repository;

    public ProveedoresRestController(ProveedorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Proveedor> listar() {
        return repository.findAll();
    }
}
