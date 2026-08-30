package com.patrones.u2;

/**
 * Fábrica concreta de la familia de reportes en formato PDF.
 * Crea instancias coherentes de {@link PdfReportBody} y {@link PdfHeaderFooter}.
 * 
 * @author Alex Rodríguez
 */
public class PdfReportFactory implements ReportFormatFactory {

    @Override
    public ReportBody createBody() {
        return new PdfReportBody();
    }

    @Override
    public ReportHeaderFooter createHeaderFooter() {
        return new PdfHeaderFooter();
    }
}
