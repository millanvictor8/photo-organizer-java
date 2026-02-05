package com.photoorganizer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Utilidad para extraer la fecha de captura de una foto usando metadatos EXIF.
 */
public class ExifDateExtractor {

    /**
     * Intenta extraer la fecha de captura de una imagen usando EXIF.
     * Devuelve Optional.empty() si no se puede obtener una fecha válida.
     */
    public static Optional<LocalDateTime> extractDate(Path imagePath) {
        File file = imagePath.toFile();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            // 1) Intentar con el directorio EXIF estándar
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (exifDirectory != null) {
                // Prioridad: Date/Time Original, luego Create Date
                LocalDateTime dateTime = getFromExifDirectory(exifDirectory);
                if (dateTime != null) {
                    return Optional.of(dateTime);
                }
            }

            // 2) Búsqueda más genérica por alguna etiqueta de fecha EXIF
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    String tagName = tag.getTagName().toLowerCase();
                    if (tagName.contains("date") || tagName.contains("time")) {
                        Optional<LocalDateTime> parsed = parseExifDate(tag.getDescription());
                        if (parsed.isPresent()) {
                            return parsed;
                        }
                    }
                }
            }
        } catch (ImageProcessingException | IOException e) {
            System.err.println("No se pudo leer metadatos EXIF de " + imagePath + ": " + e.getMessage());
        }

        return Optional.empty();
    }

    private static LocalDateTime getFromExifDirectory(ExifSubIFDDirectory exifDirectory) {
        // La librería puede devolver java.util.Date directamente
        java.util.Date exifDate = exifDirectory.getDateOriginal();
        if (exifDate == null) {
            exifDate = exifDirectory.getDateDigitized();
        }

        if (exifDate != null) {
            Instant instant = exifDate.toInstant();
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }

        // Como respaldo, intentar leer la descripción textual de las etiquetas conocidas
        String[] candidateTags = new String[]{
                ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL + "",
                ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED + ""
        };

        for (String tagId : candidateTags) {
            try {
                String description = exifDirectory.getDescription(Integer.parseInt(tagId));
                Optional<LocalDateTime> parsed = parseExifDate(description);
                if (parsed.isPresent()) {
                    return parsed.get();
                }
            } catch (NumberFormatException ignored) {
                // No debería ocurrir, pero no queremos romper la ejecución
            }
        }

        return null;
    }

    /**
     * Intenta parsear algunas variantes típicas de formato de fecha EXIF.
     */
    private static Optional<LocalDateTime> parseExifDate(String raw) {
        if (raw == null) {
            return Optional.empty();
        }

        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return Optional.empty();
        }

        // Formatos comunes EXIF, por ejemplo: "2020:05:10 14:23:11"
        String[] patterns = new String[]{
                "yyyy:MM:dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss"
        };

        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDateTime dt = LocalDateTime.parse(trimmed, formatter);
                return Optional.of(dt);
            } catch (DateTimeParseException ignored) {
                // Intentar siguiente patrón
            }
        }

        return Optional.empty();
    }
}

