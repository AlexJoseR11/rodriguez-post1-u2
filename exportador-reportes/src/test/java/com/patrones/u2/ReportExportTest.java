package com.patrones.u2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para validar el comportamiento de los patrones Abstract Factory,
 * Builder y Registry, así como la verificación estricta de invariantes de negocio.
 * 
 * @author Alex Rodríguez
 */
class ReportExportTest {

    private List<GradeRecord> sampleRecords;
    private ReportExportService service;

    @BeforeEach
    void setUp() {
        sampleRecords = Arrays.asList(
                new GradeRecord("EST-1", "Estudiante Uno", "MAT-101", 85.0),
                new GradeRecord("EST-2", "Estudiante Dos", "MAT-101", 45.0)
        );
        service = new ReportExportService();
    }

    @Test
    @DisplayName("GradeRecord: Inmutabilidad y validación de campos")
    void testGradeRecordValidation() {
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord(null, "Nombre", "MAT", 90.0));
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("ID", " ", "MAT", 90.0));
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("ID", "Nombre", "", 90.0));
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("ID", "Nombre", "MAT", -1.0));
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord("ID", "Nombre", "MAT", 101.0));

        GradeRecord record = new GradeRecord("ID-123", "Alex", "CS-101", 95.0);
        assertEquals("ID-123", record.getStudentId());
        assertEquals("Alex", record.getStudentName());
        assertEquals("CS-101", record.getCourseCode());
        assertEquals(95.0, record.getGrade());
    }

    @Test
    @DisplayName("Abstract Factory: Resolución e instanciación de familias PDF, Excel y HTML")
    void testAbstractFactoryFamilies() {
        ReportFormatFactory pdfFactory = ReportFactoryRegistry.resolve("PDF");
        assertInstanceOf(PdfReportFactory.class, pdfFactory);
        assertInstanceOf(PdfReportBody.class, pdfFactory.createBody());
        assertInstanceOf(PdfHeaderFooter.class, pdfFactory.createHeaderFooter());

        ReportFormatFactory excelFactory = ReportFactoryRegistry.resolve("excel"); // Prueba insensibilidad a mayúsculas
        assertInstanceOf(ExcelReportFactory.class, excelFactory);
        assertInstanceOf(ExcelReportBody.class, excelFactory.createBody());
        assertInstanceOf(ExcelHeaderFooter.class, excelFactory.createHeaderFooter());

        ReportFormatFactory htmlFactory = ReportFactoryRegistry.resolve("HTML");
        assertInstanceOf(HtmlReportFactory.class, htmlFactory);
        assertInstanceOf(HtmlReportBody.class, htmlFactory.createBody());
        assertInstanceOf(HtmlHeaderFooter.class, htmlFactory.createHeaderFooter());
    }

    @Test
    @DisplayName("Registry: Error controlado ante formato no registrado")
    void testRegistryUnsupportedFormat() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                ReportFactoryRegistry.resolve("XML_DESCONOCIDO"));
        assertTrue(ex.getMessage().contains("Formato no registrado"));
        assertTrue(ex.getMessage().contains("Formatos actualmente disponibles"));
    }

    @Test
    @DisplayName("Builder: Construcción válida con valores por defecto")
    void testBuilderDefaults() {
        ExportConfig config = ExportConfig.builder("PDF").build();
        assertEquals("PDF", config.getFormat());
        assertNull(config.getOutputPath());
        assertEquals("A4", config.getPageSize());
        assertEquals("PORTRAIT", config.getOrientation());
        assertNotNull(config.getLocale());
        assertNull(config.getWatermarkText());
        assertFalse(config.isIncludeLogo());
        assertFalse(config.isCompress());
        assertEquals(50, config.getMaxRowsPerPage());
    }

    @Test
    @DisplayName("Builder: Invariante compress=true requiere outputPath")
    void testBuilderCompressWithoutOutputPathThrows() {
        ExportConfig.Builder builder = ExportConfig.builder("PDF").compress(true);
        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("compress=true requiere especificar outputPath: no se puede comprimir un resultado en memoria", ex.getMessage());
    }

    @Test
    @DisplayName("Builder: Invariante maxRowsPerPage <= 0 lanza excepción")
    void testBuilderInvalidMaxRowsPerPageThrows() {
        ExportConfig.Builder builder = ExportConfig.builder("EXCEL").maxRowsPerPage(0);
        IllegalStateException ex1 = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("maxRowsPerPage debe ser mayor que 0", ex1.getMessage());

        ExportConfig.Builder builderNegative = ExportConfig.builder("EXCEL").maxRowsPerPage(-10);
        IllegalStateException ex2 = assertThrows(IllegalStateException.class, builderNegative::build);
        assertEquals("maxRowsPerPage debe ser mayor que 0", ex2.getMessage());
    }

    @Test
    @DisplayName("Builder: Constructor requiere format no nulo ni vacío")
    void testBuilderFormatValidation() {
        assertThrows(IllegalArgumentException.class, () -> ExportConfig.builder(null));
        assertThrows(IllegalArgumentException.class, () -> ExportConfig.builder("   "));
    }

    @Test
    @DisplayName("ReportExportService: Generación completa con configuración personalizada")
    void testServiceExportCustomConfig() {
        ExportConfig custom = ExportConfig.builder("PDF")
                .outputPath("/tmp/output.pdf")
                .pageSize("LETTER")
                .orientation("LANDSCAPE")
                .locale(Locale.US)
                .watermarkText("BORRADOR")
                .includeLogo(true)
                .compress(true)
                .maxRowsPerPage(10)
                .build();

        String result = service.export(custom, sampleRecords, "Institución de Prueba");
        assertNotNull(result);
        assertTrue(result.contains("PDF"));
        assertTrue(result.contains("BORRADOR"));
        assertTrue(result.contains("Institución de Prueba"));
        assertTrue(result.contains("Estudiante Uno"));
        assertTrue(result.contains("/tmp/output.pdf"));
    }
}
