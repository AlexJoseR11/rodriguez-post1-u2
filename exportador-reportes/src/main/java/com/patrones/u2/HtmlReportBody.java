package com.patrones.u2;

import java.util.List;
import java.util.Locale;

/**
 * Producto concreto para renderizar el cuerpo del reporte en HTML5 semántico.
 * Genera elementos {@code <table>}, {@code <thead>}, {@code <tbody>}, {@code <tfoot>} y clases CSS.
 * 
 * @author Alex Rodríguez
 */
public class HtmlReportBody implements ReportBody {

    @Override
    public String render(List<GradeRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("<main class=\"report-main-content\">\n");
        sb.append("  <table class=\"grades-table\" style=\"width:100%; border-collapse:collapse; font-family:sans-serif;\">\n");
        sb.append("    <thead>\n");
        sb.append("      <tr style=\"background-color:#1e3a8a; color:#ffffff;\">\n");
        sb.append("        <th style=\"padding:8px; border:1px solid #cbd5e1; text-align:left;\">ID Estudiante</th>\n");
        sb.append("        <th style=\"padding:8px; border:1px solid #cbd5e1; text-align:left;\">Nombre del Estudiante</th>\n");
        sb.append("        <th style=\"padding:8px; border:1px solid #cbd5e1; text-align:left;\">Curso</th>\n");
        sb.append("        <th style=\"padding:8px; border:1px solid #cbd5e1; text-align:right;\">Calificación</th>\n");
        sb.append("        <th style=\"padding:8px; border:1px solid #cbd5e1; text-align:center;\">Estado</th>\n");
        sb.append("      </tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");

        if (records == null || records.isEmpty()) {
            sb.append("      <tr>\n");
            sb.append("        <td colspan=\"5\" style=\"padding:12px; text-align:center; color:#64748b;\">No hay registros de calificaciones disponibles.</td>\n");
            sb.append("      </tr>\n");
        } else {
            double total = 0.0;
            for (GradeRecord r : records) {
                total += r.getGrade();
                boolean approved = r.getGrade() >= 60.0;
                String badgeStyle = approved 
                    ? "background-color:#dcfce7; color:#166534; padding:3px 8px; border-radius:4px; font-weight:bold;"
                    : "background-color:#fee2e2; color:#991b1b; padding:3px 8px; border-radius:4px; font-weight:bold;";
                String statusLabel = approved ? "APROBADO" : "REPROBADO";

                sb.append("      <tr style=\"border-bottom:1px solid #e2e8f0;\">\n");
                sb.append("        <td style=\"padding:8px; border:1px solid #cbd5e1;\">").append(escapeHtml(r.getStudentId())).append("</td>\n");
                sb.append("        <td style=\"padding:8px; border:1px solid #cbd5e1;\">").append(escapeHtml(r.getStudentName())).append("</td>\n");
                sb.append("        <td style=\"padding:8px; border:1px solid #cbd5e1;\">").append(escapeHtml(r.getCourseCode())).append("</td>\n");
                sb.append(String.format(Locale.US, "        <td style=\"padding:8px; border:1px solid #cbd5e1; text-align:right;\">%.2f</td>\n", r.getGrade()));
                sb.append("        <td style=\"padding:8px; border:1px solid #cbd5e1; text-align:center;\"><span style=\"").append(badgeStyle).append("\">").append(statusLabel).append("</span></td>\n");
                sb.append("      </tr>\n");
            }
            double average = total / records.size();
            sb.append("    </tbody>\n");
            sb.append("    <tfoot>\n");
            sb.append(String.format(Locale.US, "      <tr style=\"background-color:#f1f5f9; font-weight:bold;\">\n" +
                    "        <td colspan=\"3\" style=\"padding:8px; border:1px solid #cbd5e1;\">Total Estudiantes: %d</td>\n" +
                    "        <td colspan=\"2\" style=\"padding:8px; border:1px solid #cbd5e1; text-align:right;\">Promedio General: %.2f</td>\n" +
                    "      </tr>\n", records.size(), average));
            sb.append("    </tfoot>\n");
        }

        if (records == null || records.isEmpty()) {
            sb.append("    </tbody>\n");
        }

        sb.append("  </table>\n");
        sb.append("</main>");

        return sb.toString();
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
