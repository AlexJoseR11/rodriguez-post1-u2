package com.patrones.u2;

import java.util.List;

/**
 * Interfaz del producto abstracto para la generación del cuerpo del reporte de calificaciones.
 * Cada formato concreto (PDF, Excel, HTML) implementa su propia representación visual y semántica.
 * 
 * @author Alex Rodríguez
 */
public interface ReportBody {
    /**
     * Renderiza la lista de calificaciones en el formato específico del producto.
     *
     * @param records Lista inmutable de calificaciones a procesar.
     * @return Representación textual formateada del cuerpo del reporte.
     */
    String render(List<GradeRecord> records);
}
