
package edu.pe.cibertec.botica.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.pe.cibertec.botica.entity.Producto;
import edu.pe.cibertec.botica.repository.ProductoRepository;

@Component
public class ServidorSocket implements CommandLineRunner {

    private static final int PUERTO = 5000;

    private final ProductoRepository productoRepository;

    public ServidorSocket(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        Thread servidor = new Thread(this::iniciarServidor);
        servidor.setName("servidor-socket");
        servidor.setDaemon(true);
        servidor.start();
    }

    private void iniciarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor Socket iniciado en el puerto " + PUERTO);

            while (true) {
                Socket cliente = serverSocket.accept();
                atenderCliente(cliente);
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor Socket: " + e.getMessage());
        }
    }

    private void atenderCliente(Socket cliente) {
        try (
            Socket socket = cliente;
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            String comando = entrada.readLine();

            if (comando == null) {
                return;
            }

            comando = comando.trim();

            if (comando.equalsIgnoreCase("LISTAR")) {
                List<Producto> productos = productoRepository.findAll();

                for (Producto p : productos) {
                    salida.println(
                        p.getId() + " | " +
                        limpiar(p.getCodigo()) + " | " +
                        limpiar(p.getNombre()) + " | " +
                        p.getPrecio() + " | " +
                        p.getStock() + " | " +
                        p.getActivo()
                    );
                }

                salida.println("FIN");

            } else if (comando.toUpperCase().startsWith("BUSCAR ")) {
                String termino = comando.substring(7).trim();

                if (termino.isEmpty()) {
                    salida.println("ERROR: Debes indicar un nombre o código.");
                    return;
                }

                List<Producto> productos =
                    productoRepository
                        .findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(
                            termino, termino);

                for (Producto p : productos) {
                    salida.println(
                        p.getId() + " | " +
                        limpiar(p.getCodigo()) + " | " +
                        limpiar(p.getNombre()) + " | " +
                        p.getPrecio() + " | " +
                        p.getStock() + " | " +
                        p.getActivo()
                    );
                }

                salida.println("FIN");

            } else {
                salida.println("ERROR: Usa LISTAR o BUSCAR nombre_o_codigo");
            }

        } catch (IOException e) {
            System.err.println("Error atendiendo cliente Socket: " + e.getMessage());
        }
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace("|", "/")
                    .replace("\r", " ")
                    .replace("\n", " ");
    }
}
