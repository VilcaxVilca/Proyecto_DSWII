package edu.pe.cibertec.botica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pe.cibertec.botica.entity.Cliente;
import edu.pe.cibertec.botica.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClientesRestController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    } //http://localhost:8080/api/clientes
}