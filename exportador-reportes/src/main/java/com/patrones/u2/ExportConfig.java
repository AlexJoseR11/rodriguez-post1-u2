package com.patrones.u2;

import java.util.Locale;
import java.util.Objects;

/**
 * Objeto inmutable de configuración compleja para la exportación de reportes.
 * Implementa el patrón Builder para evitar el anti-patrón de constructores telescópicos
 * y validar invariantes de negocio antes de la instanciación.
 * 
 * @author Alex Rodríguez
 */
public final class ExportConfig {

    // Parámetro obligatorio
    private final String format;

    // Parámetros opcionales (con valores por defecto)
    private final String outputPath;
    private final String pageSize;
    private final String orientation;
    private final Locale locale;
    private final String watermarkText;
    private final boolean includeLogo;
    private final boolean compress;
    private final int maxRowsPerPage;

    private ExportConfig(Builder builder) {
        this.format = builder.format;
        this.outputPath = builder.outputPath;
        this.pageSize = builder.pageSize;
        this.orientation = builder.orientation;
        this.locale = builder.locale;
        this.watermarkText = builder.watermarkText;
        this.includeLogo = builder.includeLogo;
        this.compress = builder.compress;
        this.maxRowsPerPage = builder.maxRowsPerPage;
    }

    public static Builder builder(String format) {
        return new Builder(format);
    }

    public String getFormat() {
        return format;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getOrientation() {
        return orientation;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getWatermarkText() {
        return watermarkText;
    }

    public boolean isIncludeLogo() {
        return includeLogo;
    }

    public boolean isCompress() {
        return compress;
    }

    public int getMaxRowsPerPage() {
        return maxRowsPerPage;
    }

    @Override
    public String toString() {
        return "ExportConfig{" +
                "format='" + format + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", orientation='" + orientation + '\'' +
                ", locale=" + locale +
                ", watermarkText='" + watermarkText + '\'' +
                ", includeLogo=" + includeLogo +
                ", compress=" + compress +
                ", maxRowsPerPage=" + maxRowsPerPage +
                '}';
    }

    /**
     * Builder estático para construir instancias válidas e inmutables de {@link ExportConfig}.
     */
    public static class Builder {
        // Obligatorio
        private final String format;

        // Opcionales con valores por defecto bien definidos
        private String outputPath = null;
        private String pageSize = "A4";
        private String orientation = "PORTRAIT";
        private Locale locale = Locale.forLanguageTag("es-CO");
        private String watermarkText = null;
        private boolean includeLogo = false;
        private boolean compress = false;
        private int maxRowsPerPage = 50;

        /**
         * Constructor que exige el único parámetro estrictamente obligatorio.
         *
         * @param format Identificador del formato destino (ej. "PDF", "EXCEL", "HTML").
         */
        public Builder(String format) {
            if (format == null || format.trim().isEmpty()) {
                throw new IllegalArgumentException("El formato es obligatorio y no puede ser nulo ni vacío.");
            }
            this.format = format.trim();
        }

        public Builder outputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public Builder pageSize(String pageSize) {
            if (pageSize != null && !pageSize.trim().isEmpty()) {
                this.pageSize = pageSize.trim().toUpperCase(Locale.ROOT);
            }
            return this;
        }

        public Builder orientation(String orientation) {
            if (orientation != null && !orientation.trim().isEmpty()) {
                this.orientation = orientation.trim().toUpperCase(Locale.ROOT);
            }
            return this;
        }

        public Builder locale(Locale locale) {
            if (locale != null) {
                this.locale = locale;
            }
            return this;
        }

        public Builder locale(String languageTag) {
            if (languageTag != null && !languageTag.trim().isEmpty()) {
                this.locale = Locale.forLanguageTag(languageTag.trim());
            }
            return this;
        }

        public Builder watermarkText(String watermarkText) {
            this.watermarkText = watermarkText;
            return this;
        }

        public Builder includeLogo(boolean includeLogo) {
            this.includeLogo = includeLogo;
            return this;
        }

        public Builder compress(boolean compress) {
            this.compress = compress;
            return this;
        }

        public Builder maxRowsPerPage(int maxRowsPerPage) {
            this.maxRowsPerPage = maxRowsPerPage;
            return this;
        }

        /**
         * Valida todos los invariantes de negocio antes de retornar el objeto inmutable.
         *
         * @return Instancia configurada e inmutable de {@link ExportConfig}.
         * @throws IllegalStateException Si no se cumplen las reglas e invariantes de negocio.
         */
        public ExportConfig build() {
            // Invariante 1: Si compress es true, outputPath es obligatorio
            if (this.compress && (this.outputPath == null || this.outputPath.trim().isEmpty())) {
                throw new IllegalStateException("compress=true requiere especificar outputPath: no se puede comprimir un resultado en memoria");
            }

            // Invariante 2: maxRowsPerPage debe ser estrictamente positivo
            if (this.maxRowsPerPage <= 0) {
                throw new IllegalStateException("maxRowsPerPage debe ser mayor que 0");
            }

            return new ExportConfig(this);
        }
    }
}
