package edu.pe.cibertec.botica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pe.cibertec.botica.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	 //CONSULTAS DERIVADAS =============================
	Cliente findByDni(String dni);
    
    Cliente findByCorreo(String correo);

    List<Cliente> findByApellidos(String apellidos);

    List<Cliente> findByApellidosContaining(String texto);

    List<Cliente> findByNombresStartingWith(String texto);
    
 // CONSULTAS JPQL =============================
    @Query("SELECT c FROM Cliente c")
    List<Cliente> listarTodosJPQL();

    @Query("SELECT c\r\n FROM Cliente c\r\n WHERE c.apellidos LIKE %:apellido%")
    List<Cliente> buscarPorApellidoJPQL(@Param("apellido") String apellido);
    
    @Query("SELECT c FROM Cliente c WHERE c.apellidos LIKE %:apellido% AND c.estado = :estado")
    		List<Cliente> buscarPorApellidoYEstado(@Param("apellido") String apellido, @Param("estado") Boolean estado);
    
    
}