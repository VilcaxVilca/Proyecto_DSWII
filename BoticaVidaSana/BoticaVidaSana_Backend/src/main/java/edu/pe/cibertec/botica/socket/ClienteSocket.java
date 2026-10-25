
package edu.pe.cibertec.botica.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteSocket {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {

        try (Scanner teclado = new Scanner(System.in)) {

            System.out.println("=== CLIENTE SOCKET BOTICA VIDA SANA ===");
            System.out.println("1. Listar productos");
            System.out.println("2. Buscar producto");
            System.out.print("Seleccione una opción: ");

            String opcion = teclado.nextLine().trim();
            String comando;

            if (opcion.equals("1")) {
                comando = "LISTAR";
            } else if (opcion.equals("2")) {
                System.out.print("Ingrese nombre o código: ");
                comando = "BUSCAR " + teclado.nextLine().trim();
            } else {
                System.out.println("Opción no válida.");
                return;
            }

            try (Socket socket = new Socket(HOST, PUERTO);
                 PrintWriter salida = new PrintWriter(
                     socket.getOutputStream(), true, StandardCharsets.UTF_8);
                 BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(
                         socket.getInputStream(), StandardCharsets.UTF_8))) {

                salida.println(comando);

                String respuesta;

                while ((respuesta = entrada.readLine()) != null) {
                    if (respuesta.equals("FIN")) {
                        break;
                    }

                    System.out.println(respuesta);
                }
            }

        } catch (IOException e) {
            System.err.println(
                "No se pudo conectar al servidor Socket. " +
                "Verifica que Spring Boot esté ejecutándose en el puerto 5000.");
        }
    }
}
