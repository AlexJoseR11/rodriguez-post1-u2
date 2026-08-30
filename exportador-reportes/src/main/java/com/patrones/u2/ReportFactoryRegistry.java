package com.patrones.u2;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Registro dinámico y centralizado de fábricas de reportes que implementa el principio
 * Abierto/Cerrado (Open/Closed Principle - OCP).
 * 
 * Permite registrar y resolver fábricas de formatos en tiempo de ejecución sin modificar
 * el código existente del servicio cliente.
 * 
 * @author Alex Rodríguez
 */
public final class ReportFactoryRegistry {

    private static final Map<String, Supplier<ReportFormatFactory>> REGISTRY = new ConcurrentHashMap<>();

    static {
        // Registro inicial de las familias de formatos por defecto
        register("PDF", PdfReportFactory::new);
        register("EXCEL", ExcelReportFactory::new);
        register("HTML", HtmlReportFactory::new);
    }

    /**
     * Constructor privado para impedir la instanciación de esta clase de utilidad/registro.
     */
    private ReportFactoryRegistry() {
        throw new UnsupportedOperationException("ReportFactoryRegistry es una clase de registro estática y no debe instanciarse.");
    }

    /**
     * Registra una fábrica proveedora para un formato específico.
     *
     * @param format          Identificador del formato (ej. "PDF", "EXCEL", "HTML", "CSV").
     * @param factorySupplier Proveedor perezoso (Supplier) de la fábrica correspondiente.
     */
    public static void register(String format, Supplier<ReportFormatFactory> factorySupplier) {
        if (format == null || format.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador de formato no puede ser nulo ni vacío.");
        }
        Objects.requireNonNull(factorySupplier, "El proveedor de fábrica (Supplier) no puede ser nulo.");
        
        REGISTRY.put(format.trim().toUpperCase(Locale.ROOT), factorySupplier);
    }

    /**
     * Resuelve y retorna una nueva instancia de la fábrica correspondiente al formato solicitado.
     *
     * @param format Formato solicitado (no sensible a mayúsculas/minúsculas).
     * @return Instancia de {@link ReportFormatFactory}.
     * @throws IllegalArgumentException Si el formato es nulo, vacío o no se encuentra registrado.
     */
    public static ReportFormatFactory resolve(String format) {
        if (format == null || format.trim().isEmpty()) {
            throw new IllegalArgumentException("El formato no puede ser nulo ni vacío.");
        }

        String normalizedFormat = format.trim().toUpperCase(Locale.ROOT);
        Supplier<ReportFormatFactory> supplier = REGISTRY.get(normalizedFormat);

        if (supplier == null) {
            throw new IllegalArgumentException(String.format(
                    "Formato no registrado: '%s'. Formatos actualmente disponibles: %s",
                    format, getAvailableFormats()));
        }

        return supplier.get();
    }

    /**
     * Retorna el conjunto inmutable de formatos disponibles en el registro.
     *
     * @return Conjunto inmutable ordenado con los nombres de formatos soportados.
     */
    public static Set<String> getAvailableFormats() {
        return Collections.unmodifiableSet(new TreeSet<>(REGISTRY.keySet()));
    }
}
