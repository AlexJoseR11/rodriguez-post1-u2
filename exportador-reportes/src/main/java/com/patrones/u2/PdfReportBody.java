package com.patrones.u2;

import java.util.List;
import java.util.Locale;

/**
 * Producto concreto para renderizar el cuerpo del reporte en formato estilizado PDF.
 * Genera una estructura tabular con delimitadores fijos, alineación tipográfica y cálculo de métricas.
 * 
 * @author Alex Rodríguez
 */
public class PdfReportBody implements ReportBody {

    @Override
    public String render(List<GradeRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("+-----------------------------------------------------------------------------+\n");
        sb.append("| [PDF CONTENT STREAM] - LISTADO OFICIAL DE CALIFICACIONES                   |\n");
        sb.append("+------------+--------------------------------+------------+------------------+\n");
        sb.append("| CÓDIGO     | NOMBRE DEL ESTUDIANTE          | ASIGNATURA | NOTA / ESTADO    |\n");
        sb.append("+------------+--------------------------------+------------+------------------+\n");

        if (records == null || records.isEmpty()) {
            sb.append("|                     (Sin registros de calificaciones disponibles)          |\n");
            sb.append("+------------+--------------------------------+------------+------------------+\n");
            return sb.toString();
        }

        double totalGrade = 0.0;
        int approvedCount = 0;

        for (GradeRecord r : records) {
            totalGrade += r.getGrade();
            boolean approved = r.getGrade() >= 60.0; // Criterio académico estándar (o 3.0/60%)
            if (approved) approvedCount++;
            String status = approved ? "APROBADO" : "REPROBADO";
            String gradeText = String.format(Locale.US, "%.2f (%s)", r.getGrade(), status);

            sb.append(String.format(Locale.US, "| %-10s | %-30s | %-10s | %-16s |\n",
                    truncate(r.getStudentId(), 10),
                    truncate(r.getStudentName(), 30),
                    truncate(r.getCourseCode(), 10),
                    truncate(gradeText, 16)));
        }

        sb.append("+------------+--------------------------------+------------+------------------+\n");
        double average = totalGrade / records.size();
        sb.append(String.format(Locale.US, "| TOTAL REGISTROS: %-4d | PROMEDIO: %-5.2f | APROBADOS: %-3d | REPROBADOS: %-3d |\n",
                records.size(), average, approvedCount, (records.size() - approvedCount)));
        sb.append("+-----------------------------------------------------------------------------+\n");

        return sb.toString();
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 2) + "..";
    }
}
