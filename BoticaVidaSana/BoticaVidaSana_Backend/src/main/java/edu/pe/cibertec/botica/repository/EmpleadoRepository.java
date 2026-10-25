package edu.pe.cibertec.botica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pe.cibertec.botica.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
	
	Empleado findByDni(String dni);

    List<Empleado> findByApellidos(String apellidos);

    List<Empleado> findByApellidosContaining(String texto);
}
