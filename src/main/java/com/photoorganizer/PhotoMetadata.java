package com.photoorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Clase para extraer y gestionar metadatos de fotos.
 * Utiliza propiedades EXIF básicas sin dependencias externas.
 */
public class PhotoMetadata {
    private Path filePath;
    private LocalDateTime photoDate;
    private String extension;

    /**
     * Constructor que recibe la ruta del archivo de foto
     */
    public PhotoMetadata(Path filePath) {
        this.filePath = filePath;
        this.extension = getFileExtension(filePath);
        extractMetadata();
    }

    /**
     * Extrae los metadatos de la foto.
     * Prioriza la fecha EXIF y usa la fecha de modificación del archivo como respaldo.
     */
    private void extractMetadata() {
        // 1) Intentar obtener la fecha desde EXIF
        Optional<LocalDateTime> exifDate = ExifDateExtractor.extractDate(filePath);

        if (exifDate.isPresent()) {
            photoDate = exifDate.get();
        } else {
            // 2) Si no hay EXIF, usar fecha de modificación del archivo
            try {
                long lastModified = Files.getLastModifiedTime(filePath).toMillis();
                photoDate = LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(lastModified),
                        java.time.ZoneId.systemDefault()
                );
            } catch (IOException e) {
                System.err.println("Error al obtener fecha de modificación de " + filePath + ": " + e.getMessage());
                // 3) Último recurso: fecha actual para no romper el flujo
                photoDate = LocalDateTime.now();
            }
        }

        System.out.println("Metadatos extraídos para: " + filePath.getFileName()
                + " (Fecha: " + photoDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ")");
    }

    /**
     * Obtiene la extensión del archivo
     */
    private String getFileExtension(Path path) {
        String filename = path.getFileName().toString();
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            return filename.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Obtiene la fecha de la foto
     */
    public LocalDateTime getPhotoDate() {
        return photoDate;
    }

    /**
     * Obtiene el mes y año de la foto
     */
    public YearMonth getYearMonth() {
        return YearMonth.from(photoDate);
    }

    /**
     * Obtiene la ruta del archivo
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Obtiene el nombre del archivo
     */
    public String getFileName() {
        return filePath.getFileName().toString();
    }

    /**
     * Obtiene la extensión del archivo
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Verifica si el archivo es una foto soportada
     */
    public static boolean isSupportedPhotoFormat(String filename) {
        String extension = filename.toLowerCase();
        return extension.endsWith(".jpg") || 
               extension.endsWith(".jpeg") || 
               extension.endsWith(".png") || 
               extension.endsWith(".gif") || 
               extension.endsWith(".bmp") || 
               extension.endsWith(".webp") ||
               extension.endsWith(".tiff") ||
               extension.endsWith(".raw");
    }

    @Override
    public String toString() {
        return "PhotoMetadata{" +
                "file=" + filePath.getFileName() +
                ", date=" + photoDate +
                ", extension='" + extension + '\'' +
                '}';
    }
}
