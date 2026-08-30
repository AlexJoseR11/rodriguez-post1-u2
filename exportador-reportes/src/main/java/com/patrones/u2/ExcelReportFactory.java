package com.patrones.u2;

/**
 * Fábrica concreta de la familia de reportes en formato Excel (hojas de cálculo).
 * Crea instancias coherentes de {@link ExcelReportBody} y {@link ExcelHeaderFooter}.
 * 
 * @author Alex Rodríguez
 */
public class ExcelReportFactory implements ReportFormatFactory {

    @Override
    public ReportBody createBody() {
        return new ExcelReportBody();
    }

    @Override
    public ReportHeaderFooter createHeaderFooter() {
        return new ExcelHeaderFooter();
    }
}
