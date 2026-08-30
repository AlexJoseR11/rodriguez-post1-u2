package com.patrones.u2;

/**
 * Interfaz del producto abstracto para la generación coherente de encabezados y pies de página.
 * Garantiza que la identidad visual institucional y la numeración sigan las reglas del formato destino.
 * 
 * @author Alex Rodríguez
 */
public interface ReportHeaderFooter {
    /**
     * Renderiza el encabezado del reporte con el nombre de la institución.
     *
     * @param institutionName Nombre de la institución educativa emisora.
     * @return Cadena formateada correspondiente al encabezado.
     */
    String renderHeader(String institutionName);

    /**
     * Renderiza el pie de página del reporte con el número de página.
     *
     * @param pageNumber Número de la página actual.
     * @return Cadena formateada correspondiente al pie de página.
     */
    String renderFooter(int pageNumber);
}
