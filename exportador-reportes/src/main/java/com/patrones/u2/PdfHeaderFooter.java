package com.patrones.u2;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Producto concreto para renderizar encabezados y pies de página en formato PDF.
 * Incorpora elementos típicos de documentos PDF como encabezados corporativos con márgenes
 * y pies de página con paginación formal.
 * 
 * @author Alex Rodríguez
 */
public class PdfHeaderFooter implements ReportHeaderFooter {

    @Override
    public String renderHeader(String institutionName) {
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        return "===============================================================================\n" +
               " [PDF HEADER] %PDF-1.7 (Documento Académico Oficial)\n" +
               " INSTITUCIÓN : " + institutionName.toUpperCase() + "\n" +
               " EMISIÓN     : " + timestamp + "\n" +
               " SEGURIDAD   : FIRMA DIGITALIZADA / VERIFICACIÓN SHA-256\n" +
               "===============================================================================";
    }

    @Override
    public String renderFooter(int pageNumber) {
        return "-------------------------------------------------------------------------------\n" +
               " [PDF FOOTER] Página " + pageNumber + " | Validez académica garantizada por el Consejo Directivo\n" +
               "===============================================================================";
    }
}
