package edu.pe.cibertec.botica.service;

import java.util.List;

import org.springframework.data.domain.Page;

import edu.pe.cibertec.botica.entity.Cliente;

public interface ClienteService {

	// Consultas Derivadas ==========================
    List<Cliente> listar();

    Cliente buscarPorId(Long id);

    Cliente buscarPorDni(String dni);
    
    Cliente buscarPorCorreo(String correo);   

    List<Cliente> buscarPorApellidos(String apellidos);

    List<Cliente> buscarPorApellidosConteniendo(String texto);

    List<Cliente> buscarPorNombresIniciando(String texto);
    
    // Consultas JPQL ==========================
    List<Cliente> listarTodosJPQL();

    List<Cliente> buscarPorApellidoJPQL(String apellido);
    
    List<Cliente> buscarPorApellidoYEstado(String apellido, Boolean estado);
    
    // Ordenamiento ==========================    
    //importar todo desde org.springframework.data.domain
    List<Cliente> listarOrdenadoPorApellidos();

    List<Cliente> listarOrdenadoPorNombresDesc();
    
    // Paginación ==========================
    Page<Cliente> listarPaginado(int pagina, int tamanio);    
    
    // MANTENIMENTOS: POST, PUT, DELETE ==========================
    Cliente guardar(Cliente cliente);

    Cliente actualizar(Long id, Cliente cliente);

    void eliminar(Long id);
}