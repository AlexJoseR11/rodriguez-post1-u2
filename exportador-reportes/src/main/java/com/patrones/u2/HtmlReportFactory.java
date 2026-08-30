package com.patrones.u2;

/**
 * Fábrica concreta de la familia de reportes en formato HTML5.
 * Crea instancias coherentes de {@link HtmlReportBody} y {@link HtmlHeaderFooter}.
 * 
 * @author Alex Rodríguez
 */
public class HtmlReportFactory implements ReportFormatFactory {

    @Override
    public ReportBody createBody() {
        return new HtmlReportBody();
    }

    @Override
    public ReportHeaderFooter createHeaderFooter() {
        return new HtmlHeaderFooter();
    }
}
