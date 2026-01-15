package com.photoorganizer;

/**
 * Clase principal de la aplicación Photo Organizer.
 * 
 * Uso:
 *   java -cp target/photo-organizer.jar com.photoorganizer.Main
 * 
 * Antes de ejecutar, configure los directorios en src/main/resources/config.properties
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║     Photo Organizer v1.0          ║");
            System.out.println("║  Organiza fotos por año y mes      ║");
            System.out.println("╚════════════════════════════════════╝\n");
            
            // Cargar configuración
            Config config = new Config();
            
            // Crear organizador de fotos
            PhotoOrganizer organizer = new PhotoOrganizer(config);
            
            // Ejecutar organización
            organizer.organize();
            
            System.out.println("¡Proceso completado!");
            
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
