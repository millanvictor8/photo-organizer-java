package com.photoorganizer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Clase para gestionar la configuración de la aplicación.
 * Lee las propiedades del archivo config.properties
 */
public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;
    private Path sourceDirectory;
    private Path destinationDirectory;
    private String folderStructure;

    /**
     * Constructor que carga la configuración desde el archivo de propiedades
     */
    public Config() {
        this.properties = new Properties();
        loadConfig();
    }

    /**
     * Carga la configuración desde el archivo config.properties
     */
    private void loadConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Archivo de configuración no encontrado: " + CONFIG_FILE);
            }
            properties.load(input);
            
            // Validar propiedades requeridas
            String source = properties.getProperty("source.directory");
            String destination = properties.getProperty("destination.directory");
            
            if (source == null || source.isBlank()) {
                throw new IllegalArgumentException("source.directory no está configurado");
            }
            if (destination == null || destination.isBlank()) {
                throw new IllegalArgumentException("destination.directory no está configurado");
            }
            
            this.sourceDirectory = Paths.get(source);
            this.destinationDirectory = Paths.get(destination);
            this.folderStructure = properties.getProperty("folder.structure", "YYYY/MM");
            
            System.out.println("Configuración cargada correctamente:");
            System.out.println("  Origen: " + sourceDirectory);
            System.out.println("  Destino: " + destinationDirectory);
            System.out.println("  Estructura: " + folderStructure);
            
        } catch (IOException e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Obtiene el directorio de origen
     */
    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    /**
     * Obtiene el directorio de destino
     */
    public Path getDestinationDirectory() {
        return destinationDirectory;
    }

    /**
     * Obtiene la estructura de carpetas
     */
    public String getFolderStructure() {
        return folderStructure;
    }
}
