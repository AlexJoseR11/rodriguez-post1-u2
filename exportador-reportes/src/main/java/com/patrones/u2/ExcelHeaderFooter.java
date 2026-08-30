package com.patrones.u2;

/**
 * Producto concreto para renderizar encabezados y pies de página en formato Excel.
 * Utiliza convenciones y códigos de marcado propios de cabeceras de libros de cálculo (&L, &C, &R, &P).
 * 
 * @author Alex Rodríguez
 */
public class ExcelHeaderFooter implements ReportHeaderFooter {

    @Override
    public String renderHeader(String institutionName) {
        return "[EXCEL_CONFIG_PAGINA: ENCABEZADO]\n" +
               "&L&\"Arial,Negrita\"" + institutionName + "\t&C&\"Arial,Normal\"REPORTE DE RENDIMIENTO ACADÉMICO\t&R&D - &T";
    }

    @Override
    public String renderFooter(int pageNumber) {
        return "[EXCEL_CONFIG_PAGINA: PIE_PAGINA]\n" +
               "&L&F (Libro de Calificaciones)\t&C&\"Arial,Cursiva\"Confidencial\t&RPágina " + pageNumber + " de &N";
    }
}
