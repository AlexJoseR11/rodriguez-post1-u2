package com.patrones.u2;

/**
 * Contrato Abstract Factory para la creación de familias completas y compatibles
 * de componentes de reporte (ReportBody y ReportHeaderFooter).
 * 
 * Cada fábrica concreta es responsable de instanciar productos pertenecientes
 * a una misma variante de formato (PDF, Excel, HTML, etc.), evitando incompatibilidades.
 * 
 * @author Alex Rodríguez
 */
public interface ReportFormatFactory {
    /**
     * Crea una instancia concreta del cuerpo del reporte para la familia específica.
     *
     * @return Implementación concreta de {@link ReportBody}.
     */
    ReportBody createBody();

    /**
     * Crea una instancia concreta de encabezado/pie de página para la familia específica.
     *
     * @return Implementación concreta de {@link ReportHeaderFooter}.
     */
    ReportHeaderFooter createHeaderFooter();
}
