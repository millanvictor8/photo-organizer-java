#!/bin/bash
# Script de compilación para Photo Organizer

set -e

echo "========================================="
echo "Compilando Photo Organizer (Java 21)"
echo "========================================="
echo ""

# Compilar proyecto
if command -v mvn &> /dev/null; then
    echo "Usando Maven para compilar..."
    mvn clean package
    echo ""
    echo "✓ Compilación exitosa!"
    echo "JAR generado: target/photo-organizer.jar"
else
    echo "Maven no encontrado. Compilando manualmente con javac..."
    
    # Crear directorio de salida
    mkdir -p build
    
    # Compilar código Java
    javac -d build -encoding UTF-8 \
        src/main/java/com/photoorganizer/*.java
    
    # Copiar recursos
    cp src/main/resources/config.properties build/
    
    # Crear JAR
    cd build
    jar cfe ../photo-organizer.jar com.photoorganizer.Main com/photoorganizer/*.class config.properties
    cd ..
    
    echo "✓ Compilación exitosa!"
    echo "JAR generado: photo-organizer.jar"
fi

echo ""
echo "Para ejecutar: java -jar target/photo-organizer.jar"
