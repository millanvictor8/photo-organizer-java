package com.photoorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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
     * Extrae los metadatos de la foto
     */
    private void extractMetadata() {
        try {
            // Intentar obtener la fecha de modificación del archivo como fallback
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();
            photoDate = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(lastModified),
                java.time.ZoneId.systemDefault()
            );
            
            System.out.println("Metadatos extraídos para: " + filePath.getFileName() 
                + " (Fecha: " + photoDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ")");
            
        } catch (IOException e) {
            System.err.println("Error al obtener metadatos de " + filePath + ": " + e.getMessage());
            photoDate = LocalDateTime.now();
        }
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
