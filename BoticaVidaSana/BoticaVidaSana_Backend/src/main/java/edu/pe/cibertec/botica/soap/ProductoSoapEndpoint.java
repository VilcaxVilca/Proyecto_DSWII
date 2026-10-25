
package edu.pe.cibertec.botica.soap;

import java.util.List;
import java.util.Optional;

import javax.xml.transform.Source;

import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.xml.transform.StringSource;

import edu.pe.cibertec.botica.entity.Producto;
import edu.pe.cibertec.botica.repository.ProductoRepository;

@Endpoint
@Component
public class ProductoSoapEndpoint {

    private static final String NAMESPACE =
            "http://cibertec.pe/botica/soap";

    private final ProductoRepository productoRepository;

    public ProductoSoapEndpoint(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // 1. Consultar todos los productos
    @PayloadRoot(namespace = NAMESPACE,
            localPart = "consultarProductosRequest")
    @ResponsePayload
    public Source consultarProductos(@RequestPayload Source request) {

        List<Producto> productos = productoRepository.findAll();

        StringBuilder xml = new StringBuilder();
        xml.append("<consultarProductosResponse xmlns=\"")
           .append(NAMESPACE).append("\">");

        for (Producto p : productos) {
            agregarProducto(xml, p);
        }

        xml.append("</consultarProductosResponse>");

        return new StringSource(xml.toString());
    }

    // 2. Buscar un producto por ID
    @PayloadRoot(namespace = NAMESPACE,
            localPart = "buscarProductoPorIdRequest")
    @ResponsePayload
    public Source buscarProductoPorId(@RequestPayload Source request) {

        String idTexto = obtenerValor(request, "id");
        Long id = Long.parseLong(idTexto);

        Optional<Producto> resultado = productoRepository.findById(id);

        StringBuilder xml = new StringBuilder();
        xml.append("<buscarProductoPorIdResponse xmlns=\"")
           .append(NAMESPACE).append("\">");

        xml.append("<encontrado>")
           .append(resultado.isPresent())
           .append("</encontrado>");

        resultado.ifPresent(p -> agregarCamposProducto(xml, p));

        xml.append("</buscarProductoPorIdResponse>");

        return new StringSource(xml.toString());
    }

    // 3. Buscar productos por nombre
    @PayloadRoot(namespace = NAMESPACE,
            localPart = "buscarProductosPorNombreRequest")
    @ResponsePayload
    public Source buscarProductosPorNombre(@RequestPayload Source request) {

        String nombre = obtenerValor(request, "nombre");

        List<Producto> productos =
                productoRepository
                .findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(
                        nombre, nombre);

        StringBuilder xml = new StringBuilder();
        xml.append("<buscarProductosPorNombreResponse xmlns=\"")
           .append(NAMESPACE).append("\">");

        for (Producto p : productos) {
            agregarProducto(xml, p);
        }

        xml.append("</buscarProductosPorNombreResponse>");

        return new StringSource(xml.toString());
    }

    // 4. Contar productos
    @PayloadRoot(namespace = NAMESPACE,
            localPart = "contarProductosRequest")
    @ResponsePayload
    public Source contarProductos(@RequestPayload Source request) {

        long total = productoRepository.count();

        String xml = "<contarProductosResponse xmlns=\"" + NAMESPACE + "\">"
                + "<total>" + total + "</total>"
                + "</contarProductosResponse>";

        return new StringSource(xml);
    }

    // Agregar un producto completo a una respuesta XML
    private void agregarProducto(StringBuilder xml, Producto p) {
        xml.append("<producto>");
        agregarCamposProducto(xml, p);
        xml.append("</producto>");
    }

    private void agregarCamposProducto(StringBuilder xml, Producto p) {
        xml.append("<id>").append(p.getId()).append("</id>");
        xml.append("<codigo>").append(escapar(p.getCodigo()))
           .append("</codigo>");
        xml.append("<nombre>").append(escapar(p.getNombre()))
           .append("</nombre>");
        xml.append("<precio>").append(p.getPrecio()).append("</precio>");
        xml.append("<stock>").append(p.getStock()).append("</stock>");
        xml.append("<fechaVencimiento>")
           .append(p.getFechaVencimiento())
           .append("</fechaVencimiento>");
        xml.append("<activo>").append(p.getActivo()).append("</activo>");
    }

    // Leer un campo de la solicitud XML
    private String obtenerValor(Source request, String campo) {
        try {
            javax.xml.transform.Transformer transformer =
                    javax.xml.transform.TransformerFactory
                    .newInstance().newTransformer();

            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(
                    request,
                    new javax.xml.transform.stream.StreamResult(writer));

            String xml = writer.toString();

            java.util.regex.Matcher matcher =
                    java.util.regex.Pattern.compile(
                        "<(?:\\w+:)?" + campo + "\\b[^>]*>(.*?)</(?:\\w+:)?"
                        + campo + "\\s*>",
                        java.util.regex.Pattern.DOTALL)
                    .matcher(xml);

            if (matcher.find()) {
                return matcher.group(1).trim();
            }

            throw new IllegalArgumentException(
                    "No se encontró el campo XML: " + campo);

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "No se pudo leer el campo " + campo, e);
        }
    }

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
