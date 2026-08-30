package com.patrones.u2;

import java.util.List;
import java.util.Locale;

/**
 * Producto concreto para renderizar el cuerpo del reporte en formato estructurado Excel / Hoja de cálculo.
 * Representa filas y celdas delimitadas con tabuladores y fórmulas típicas de hojas de cálculo.
 * 
 * @author Alex Rodríguez
 */
public class ExcelReportBody implements ReportBody {

    @Override
    public String render(List<GradeRecord> records) {
        StringBuilder sb = new StringBuilder();
        sb.append("[HOJA_EXCEL: Calificaciones_Consolidadas]\n");
        sb.append("A1:ID_ESTUDIANTE\tB1:NOMBRE_COMPLETO\tC1:COD_ASIGNATURA\tD1:CALIFICACION\tE1:ESTADO\n");

        if (records == null || records.isEmpty()) {
            sb.append("(Hoja vacía sin registros de datos)\n");
            return sb.toString();
        }

        int row = 2;
        for (GradeRecord r : records) {
            sb.append(String.format(Locale.US, "A%d:%s\tB%d:\"%s\"\tC%d:%s\tD%d:%.2f\tE%d:=SI(D%d>=60;\"APROBADO\";\"REPROBADO\")\n",
                    row, r.getStudentId(),
                    row, r.getStudentName(),
                    row, r.getCourseCode(),
                    row, r.getGrade(),
                    row, row));
            row++;
        }

        sb.append(String.format(Locale.US, "A%d:TOTAL_FILAS\tB%d:=CONTAR(D2:D%d)\tC%d:PROMEDIO_GENERAL\tD%d:=PROMEDIO(D2:D%d)\tE%d:---\n",
                row, row, row - 1, row, row, row - 1, row));

        return sb.toString();
    }
}
