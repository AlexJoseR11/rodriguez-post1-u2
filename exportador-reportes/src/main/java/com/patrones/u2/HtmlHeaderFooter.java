package com.patrones.u2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Producto concreto para renderizar encabezados y pies de página en formato HTML5.
 * Estructura las etiquetas semánticas {@code <header>} y {@code <footer>} con estilos integrados.
 * 
 * @author Alex Rodríguez
 */
public class HtmlHeaderFooter implements ReportHeaderFooter {

    @Override
    public String renderHeader(String institutionName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return "<!DOCTYPE html>\n" +
               "<html lang=\"es\">\n" +
               "<head>\n" +
               "  <meta charset=\"UTF-8\">\n" +
               "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "  <title>Reporte Académico - " + escapeHtml(institutionName) + "</title>\n" +
               "</head>\n" +
               "<body style=\"font-family: Arial, sans-serif; margin: 20px; color: #1e293b;\">\n" +
               "<header class=\"report-header\" style=\"border-bottom: 2px solid #1e3a8a; padding-bottom: 12px; margin-bottom: 20px;\">\n" +
               "  <h1 style=\"color: #1e3a8a; margin: 0; font-size: 24px;\">" + escapeHtml(institutionName) + "</h1>\n" +
               "  <p style=\"color: #64748b; margin: 4px 0 0 0; font-size: 14px;\">Reporte Oficial Consolidado de Notas | Generado: " + timestamp + "</p>\n" +
               "</header>";
    }

    @Override
    public String renderFooter(int pageNumber) {
        return "<footer class=\"report-footer\" style=\"margin-top: 30px; padding-top: 10px; border-top: 1px solid #cbd5e1; font-size: 12px; color: #64748b; display: flex; justify-content: space-between;\">\n" +
               "  <span>Documento emitido por el Sistema de Información Académica</span>\n" +
               "  <span>Página " + pageNumber + "</span>\n" +
               "</footer>\n" +
               "</body>\n" +
               "</html>";
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
