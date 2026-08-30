package com.patrones.u2;

import java.util.List;
import java.util.Objects;

/**
 * Servicio cliente de alto nivel para la exportación de reportes académicos.
 * Orquesta el uso de {@link ReportFactoryRegistry} y {@link ExportConfig} desacoplando
 * la lógica de negocio de los detalles de implementación de cada formato.
 * 
 * @author Alex Rodríguez
 */
public class ReportExportService {

    /**
     * Exporta un reporte utilizando la configuración básica de formato.
     *
     * @param format          Identificador del formato (ej. "PDF", "EXCEL", "HTML").
     * @param records         Lista de calificaciones a incluir en el reporte.
     * @param institutionName Nombre oficial de la institución educativa.
     * @return Contenido completo del reporte generado.
     */
    public String export(String format, List<GradeRecord> records, String institutionName) {
        ExportConfig config = ExportConfig.builder(format).build();
        return export(config, records, institutionName);
    }

    /**
     * Exporta un reporte aplicando una configuración avanzada personalizada.
     *
     * @param config          Objeto {@link ExportConfig} inmutable con los parámetros de exportación.
     * @param records         Lista de calificaciones a incluir en el reporte.
     * @param institutionName Nombre oficial de la institución educativa.
     * @return Contenido completo del reporte generado.
     */
    public String export(ExportConfig config, List<GradeRecord> records, String institutionName) {
        Objects.requireNonNull(config, "La configuración de exportación (ExportConfig) no puede ser nula.");
        Objects.requireNonNull(records, "La lista de registros de notas (records) no puede ser nula.");
        if (institutionName == null || institutionName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la institución no puede ser nulo ni vacío.");
        }

        // 1. Resolver la fábrica abstracta dinámicamente mediante el Registro (OCP)
        ReportFormatFactory factory = ReportFactoryRegistry.resolve(config.getFormat());

        // 2. Crear la familia coherente de productos abstractos
        ReportBody body = factory.createBody();
        ReportHeaderFooter headerFooter = factory.createHeaderFooter();

        // 3. Ensamblar las partes del reporte
        StringBuilder report = new StringBuilder();

        // Metadatos de configuración decorativos si no es formato por defecto
        if (config.getWatermarkText() != null || config.isIncludeLogo() || !"A4".equalsIgnoreCase(config.getPageSize()) || config.isCompress() || config.getOutputPath() != null) {
            report.append(buildConfigBanner(config)).append("\n");
        }

        // Encabezado
        report.append(headerFooter.renderHeader(institutionName.trim())).append("\n");

        // Marca de agua si aplica
        if (config.getWatermarkText() != null && !config.getWatermarkText().trim().isEmpty()) {
            report.append(">>> MARCA DE AGUA: [ ").append(config.getWatermarkText().trim().toUpperCase()).append(" ] <<<\n");
        }

        // Cuerpo con los registros
        report.append(body.render(records)).append("\n");

        // Cálculo de paginación
        int totalRows = records.size();
        int maxRows = config.getMaxRowsPerPage();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalRows / maxRows));

        // Pie de página
        report.append(headerFooter.renderFooter(totalPages));

        // Información de salida en disco si fue especificado
        if (config.getOutputPath() != null && !config.getOutputPath().trim().isEmpty()) {
            report.append("\n[DESTINO DE EXPORTACIÓN]: Archivo generado en -> ").append(config.getOutputPath());
            if (config.isCompress()) {
                report.append(" (Compresión GZIP/ZIP habilitada)");
            }
        }

        return report.toString();
    }

    private String buildConfigBanner(ExportConfig config) {
        return String.format("[METADATOS DE IMPRESIÓN | Tamaño: %s | Orientación: %s | Idioma: %s | Logo: %s | MáxFilas/Pág: %d]",
                config.getPageSize(),
                config.getOrientation(),
                config.getLocale().toLanguageTag(),
                config.isIncludeLogo() ? "SÍ" : "NO",
                config.getMaxRowsPerPage());
    }
}
