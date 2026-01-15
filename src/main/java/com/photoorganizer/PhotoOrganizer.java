package com.photoorganizer;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal para organizar las fotos según sus metadatos.
 */
public class PhotoOrganizer {
    private Config config;
    private int processedCount;
    private int errorCount;

    /**
     * Constructor que recibe la configuración
     */
    public PhotoOrganizer(Config config) {
        this.config = config;
        this.processedCount = 0;
        this.errorCount = 0;
    }

    /**
     * Inicia el proceso de organización de fotos
     */
    public void organize() {
        System.out.println("\n========== Iniciando organización de fotos ==========\n");
        
        // Validar directorios
        if (!Files.exists(config.getSourceDirectory())) {
            System.err.println("Error: El directorio de origen no existe: " + config.getSourceDirectory());
            return;
        }

        // Crear directorio de destino si no existe
        try {
            Files.createDirectories(config.getDestinationDirectory());
        } catch (IOException e) {
            System.err.println("Error al crear directorio de destino: " + e.getMessage());
            return;
        }

        // Obtener lista de fotos
        List<Path> photoFiles = findPhotoFiles(config.getSourceDirectory());
        
        if (photoFiles.isEmpty()) {
            System.out.println("No se encontraron fotos en el directorio de origen.");
            return;
        }

        System.out.println("Se encontraron " + photoFiles.size() + " fotos para organizar.\n");

        // Procesar cada foto
        for (Path photoFile : photoFiles) {
            processPhoto(photoFile);
        }

        // Mostrar resumen
        printSummary(photoFiles.size());
    }

    /**
     * Busca recursivamente todas las fotos en un directorio
     */
    private List<Path> findPhotoFiles(Path directory) {
        List<Path> photos = new ArrayList<>();
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // Búsqueda recursiva en subdirectorios
                    photos.addAll(findPhotoFiles(entry));
                } else if (PhotoMetadata.isSupportedPhotoFormat(entry.getFileName().toString())) {
                    photos.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar fotos en " + directory + ": " + e.getMessage());
        }
        
        return photos;
    }

    /**
     * Procesa una foto individual
     */
    private void processPhoto(Path photoFile) {
        try {
            // Extraer metadatos
            PhotoMetadata metadata = new PhotoMetadata(photoFile);
            
            // Calcular ruta de destino
            Path destinationPath = calculateDestinationPath(metadata);
            
            // Crear directorios si es necesario
            Files.createDirectories(destinationPath.getParent());
            
            // Copiar archivo
            Files.copy(photoFile, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("[OK] " + metadata.getFileName() + " -> " + destinationPath.getParent().getFileName());
            processedCount++;
            
        } catch (IOException e) {
            System.err.println("[ERROR] " + photoFile.getFileName() + ": " + e.getMessage());
            errorCount++;
        }
    }

    /**
     * Calcula la ruta de destino basada en los metadatos
     */
    private Path calculateDestinationPath(PhotoMetadata metadata) {
        YearMonth yearMonth = metadata.getYearMonth();
        
        // Construir ruta según estructura configurada (YYYY/MM)
        String year = String.format("%04d", yearMonth.getYear());
        String month = String.format("%02d", yearMonth.getMonthValue());
        
        Path destinationDirectory = config.getDestinationDirectory()
                .resolve(year)
                .resolve(month);
        
        return destinationDirectory.resolve(metadata.getFileName());
    }

    /**
     * Imprime un resumen del proceso
     */
    private void printSummary(int totalFound) {
        System.out.println("\n========== Resumen del proceso ==========");
        System.out.println("Total de fotos encontradas: " + totalFound);
        System.out.println("Fotos procesadas correctamente: " + processedCount);
        System.out.println("Errores encontrados: " + errorCount);
        System.out.println("========================================\n");
    }
}
