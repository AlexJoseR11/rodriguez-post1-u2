package com.patrones.u2;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Clase principal de demostración para el proyecto de Exportación de Reportes Académicos.
 * Demuestra de forma exhaustiva el funcionamiento de los patrones Abstract Factory,
 * Builder y Registry (OCP).
 * 
 * @author Alex Rodríguez
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   DEMOSTRACIÓN DE PATRONES CREACIONALES — UNIDAD 2 (Alex Rodríguez)           ");
        System.out.println("   Patrones: Abstract Factory | Builder | Registry (OCP)                        ");
        System.out.println("================================================================================\n");

        // 0. Datos de prueba: Registros académicos
        List<GradeRecord> sampleRecords = Arrays.asList(
                new GradeRecord("EST-101", "Alex Rodríguez", "CS-201", 95.5),
                new GradeRecord("EST-102", "Valeria Gómez", "CS-201", 88.0),
                new GradeRecord("EST-103", "Carlos Mendoza", "CS-201", 52.0),
                new GradeRecord("EST-104", "Sofía Fernández", "CS-201", 79.5),
                new GradeRecord("EST-105", "Diego Morales", "CS-201", 45.0),
                new GradeRecord("EST-106", "Lucía Navarro", "CS-201", 91.0)
        );

        String institution = "Universidad de Santander (UDES)";
        ReportExportService service = new ReportExportService();

        // -----------------------------------------------------------------------------------------
        // PARTE 1: Exportación en los 3 formatos base (PDF, Excel, HTML) vía Abstract Factory
        // -----------------------------------------------------------------------------------------
        System.out.println(">>> 1. EXPORTACIÓN EN FORMATOS BASE (PDF, EXCEL, HTML) <<<\n");

        System.out.println("--- [1.1 FORMATO PDF] ---");
        String pdfOutput = service.export("PDF", sampleRecords, institution);
        System.out.println(pdfOutput);
        System.out.println("\n");

        System.out.println("--- [1.2 FORMATO EXCEL] ---");
        String excelOutput = service.export("EXCEL", sampleRecords, institution);
        System.out.println(excelOutput);
        System.out.println("\n");

        System.out.println("--- [1.3 FORMATO HTML] ---");
        String htmlOutput = service.export("HTML", sampleRecords, institution);
        System.out.println(htmlOutput);
        System.out.println("\n");

        // -----------------------------------------------------------------------------------------
        // PARTE 2: Exportación usando ExportConfig con valores por defecto
        // -----------------------------------------------------------------------------------------
        System.out.println("================================================================================");
        System.out.println(">>> 2. EXPORTACIÓN CON ExportConfig (VALORES POR DEFECTO) <<<\n");

        ExportConfig defaultConfig = ExportConfig.builder("PDF").build();
        System.out.println("Configuración construida: " + defaultConfig);
        String defaultExportOutput = service.export(defaultConfig, sampleRecords, institution);
        System.out.println(defaultExportOutput);
        System.out.println("\n");

        // -----------------------------------------------------------------------------------------
        // PARTE 3: Exportación usando ExportConfig con parámetros personalizados (Builder)
        // -----------------------------------------------------------------------------------------
        System.out.println("================================================================================");
        System.out.println(">>> 3. EXPORTACIÓN CON ExportConfig PERSONALIZADA (Letter, Landscape, Watermark, etc.) <<<\n");

        ExportConfig customConfig = ExportConfig.builder("PDF")
                .outputPath("/var/reports/calificaciones_2026_u2.pdf")
                .pageSize("LETTER")
                .orientation("LANDSCAPE")
                .locale(Locale.forLanguageTag("es-CO"))
                .watermarkText("COPIA NO OFICIAL - AUDITORÍA")
                .includeLogo(true)
                .compress(true)
                .maxRowsPerPage(4)
                .build();

        System.out.println("Configuración personalizada construida:");
        System.out.println("  * Formato: " + customConfig.getFormat());
        System.out.println("  * OutputPath: " + customConfig.getOutputPath());
        System.out.println("  * PageSize: " + customConfig.getPageSize());
        System.out.println("  * Orientation: " + customConfig.getOrientation());
        System.out.println("  * Locale: " + customConfig.getLocale());
        System.out.println("  * WatermarkText: " + customConfig.getWatermarkText());
        System.out.println("  * IncludeLogo: " + customConfig.isIncludeLogo());
        System.out.println("  * Compress: " + customConfig.isCompress());
        System.out.println("  * MaxRowsPerPage: " + customConfig.getMaxRowsPerPage());
        System.out.println();

        String customExportOutput = service.export(customConfig, sampleRecords, institution);
        System.out.println(customExportOutput);
        System.out.println("\n");

        // -----------------------------------------------------------------------------------------
        // PARTE 4: Captura controlada al violar invariantes en Builder (compress=true sin outputPath)
        // -----------------------------------------------------------------------------------------
        System.out.println("================================================================================");
        System.out.println(">>> 4. VALIDACIÓN DE INVARIANTES EN BUILDER (Manejo de Errores Controlado) <<<\n");

        // Caso 4.1: compress=true sin outputPath
        try {
            System.out.println("Intentando construir ExportConfig con compress=true pero sin outputPath...");
            ExportConfig invalidConfig = ExportConfig.builder("PDF")
                    .compress(true)
                    .build(); // Debe lanzar IllegalStateException
            System.out.println("ERROR: No debería llegar aquí! Objeto creado: " + invalidConfig);
        } catch (IllegalStateException e) {
            System.out.println(" Excepción capturada exitosamente (Invariante 1):");
            System.out.println("   Tipo: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: \"" + e.getMessage() + "\"\n");
        }

        // Caso 4.2: maxRowsPerPage <= 0
        try {
            System.out.println("Intentando construir ExportConfig con maxRowsPerPage = -5...");
            ExportConfig invalidConfig2 = ExportConfig.builder("EXCEL")
                    .maxRowsPerPage(-5)
                    .build(); // Debe lanzar IllegalStateException
            System.out.println("ERROR: No debería llegar aquí! Objeto creado: " + invalidConfig2);
        } catch (IllegalStateException e) {
            System.out.println(" Excepción capturada exitosamente (Invariante 2):");
            System.out.println("   Tipo: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: \"" + e.getMessage() + "\"\n");
        }

        // Caso 4.3: format nulo o vacío en constructor del Builder
        try {
            System.out.println("Intentando construir ExportConfig con format nulo...");
            ExportConfig.builder(null);
        } catch (IllegalArgumentException e) {
            System.out.println(" Excepción capturada exitosamente (Validación Formato):");
            System.out.println("   Tipo: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: \"" + e.getMessage() + "\"\n");
        }

        // -----------------------------------------------------------------------------------------
        // PARTE 5: Demostración del Principio Abierto/Cerrado (OCP) - Nuevo Formato Dinámico
        // -----------------------------------------------------------------------------------------
        System.out.println("================================================================================");
        System.out.println(">>> 5. EXTENSIBILIDAD OCP: REGISTRO DINÁMICO DE UN NUEVO FORMATO (MARKDOWN) <<<\n");

        System.out.println("Formatos disponibles antes de la extensión: " + ReportFactoryRegistry.getAvailableFormats());

        // Registramos un nuevo formato "MARKDOWN" en caliente sin tocar una sola línea de código existente:
        ReportFactoryRegistry.register("MARKDOWN", () -> new ReportFormatFactory() {
            @Override
            public ReportBody createBody() {
                return records -> {
                    StringBuilder md = new StringBuilder();
                    md.append("| ID | Estudiante | Asignatura | Nota | Estado |\n");
                    md.append("|---|---|---|---|---|\n");
                    for (GradeRecord gr : records) {
                        String st = gr.getGrade() >= 60 ? "**Aprobado**" : "*Reprobado*";
                        md.append(String.format("| `%s` | %s | %s | %.2f | %s |\n",
                                gr.getStudentId(), gr.getStudentName(), gr.getCourseCode(), gr.getGrade(), st));
                    }
                    return md.toString();
                };
            }

            @Override
            public ReportHeaderFooter createHeaderFooter() {
                return new ReportHeaderFooter() {
                    @Override
                    public String renderHeader(String institutionName) {
                        return "# " + institutionName + "\n## Reporte de Calificaciones (Markdown)\n";
                    }

                    @Override
                    public String renderFooter(int pageNumber) {
                        return "\n---\n*Página " + pageNumber + " | Generado vía Extensión OCP*\n";
                    }
                };
            }
        });

        System.out.println("Formatos disponibles después del registro: " + ReportFactoryRegistry.getAvailableFormats());
        System.out.println("\n--- [EXPORTACIÓN EN FORMATO EXTENDIDO 'MARKDOWN'] ---");
        String markdownOutput = service.export("MARKDOWN", sampleRecords, institution);
        System.out.println(markdownOutput);

        System.out.println("\n================================================================================");
        System.out.println("   DEMOSTRACIÓN FINALIZADA CON ÉXITO");
        System.out.println("================================================================================");
    }
}
