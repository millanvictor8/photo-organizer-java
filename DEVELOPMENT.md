## Instrucciones de Desarrollo - Photo Organizer

### Estructura del Proyecto

```
photo-organizer-java/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/photoorganizer/
│       │       ├── Main.java              # Punto de entrada
│       │       ├── Config.java            # Gestión de configuración
│       │       ├── PhotoMetadata.java     # Extracción de metadatos
│       │       └── PhotoOrganizer.java    # Lógica de organización
│       └── resources/
│           └── config.properties          # Archivo de configuración
├── pom.xml                                # Configuración Maven
├── build.sh                               # Script de compilación
└── photo-organizer.jar                    # JAR compilado
```

### Compilación

#### Con Maven
```bash
mvn clean package
java -jar target/photo-organizer-jar-with-dependencies.jar
```

#### Con el script build.sh
```bash
chmod +x build.sh
./build.sh
```

#### Compilación manual
```bash
mkdir -p build
javac -d build -encoding UTF-8 src/main/java/com/photoorganizer/*.java
cp src/main/resources/config.properties build/
cd build
jar cfe ../photo-organizer.jar com.photoorganizer.Main com/photoorganizer/*.class config.properties
cd ..
java -jar photo-organizer.jar
```

### Ejecución

```bash
java -jar photo-organizer.jar
```

O con el classpath:
```bash
java -cp target/photo-organizer.jar com.photoorganizer.Main
```

### Requisitos

- Java 21 o superior
- Maven 3.6+ (opcional)
- Linux, macOS o Windows

### Clases Principales

#### Main.java
- Punto de entrada de la aplicación
- Inicializa Config y PhotoOrganizer
- Gestiona excepciones globales

#### Config.java
- Lee configuración desde config.properties
- Valida que los directorios estén especificados
- Proporciona acceso a source directory, destination directory y estructura

#### PhotoMetadata.java
- Extrae metadatos de los archivos de foto
- Obtiene la fecha de última modificación del archivo
- Soporta múltiples formatos de imagen
- Calcula YearMonth para organización

#### PhotoOrganizer.java
- Busca recursivamente fotos en el directorio de origen
- Procesa cada foto y calcula su ruta de destino
- Copia los archivos manteniendo su nombre
- Genera un resumen de la operación

### Configuración

El archivo `config.properties` define:

```properties
source.directory=<ruta de entrada>
destination.directory=<ruta de salida>
folder.structure=YYYY/MM
```

### Flujo de Ejecución

1. Main carga Config
2. Config valida rutas y carga propiedades
3. PhotoOrganizer inicia proceso
4. Se buscan fotos recursivamente
5. Para cada foto:
   - Se extraen metadatos (fecha)
   - Se calcula ruta de destino (año/mes)
   - Se copia el archivo
6. Se imprime resumen

### Testing Manual

Crear estructura de prueba:
```bash
mkdir -p /tmp/photos/{source,destination}
touch /tmp/photos/source/test_{1..5}.jpg
touch -d "2024-06-15" /tmp/photos/source/test_*.jpg

# Actualizar config.properties:
# source.directory=/tmp/photos/source
# destination.directory=/tmp/photos/destination

# Compilar y ejecutar
./build.sh
java -jar photo-organizer.jar

# Verificar resultado
tree /tmp/photos/destination/
```

### Funcionalidades Implementadas

✅ Lectura de configuración desde properties  
✅ Búsqueda recursiva de fotos  
✅ Extracción de metadatos (fecha de archivo)  
✅ Organización por año/mes  
✅ Creación de directorios automática  
✅ Reporte de progreso y errores  
✅ Soporte para múltiples formatos  

### Funcionalidades Futuras

- Extracción completa de EXIF
- Renombrado avanzado de archivos
- Filtrado por rango de fechas
- GUI con JavaFX
- Logging detallado
- Configuración de permisos de archivos
- Deduplicación de fotos

### Notas

- Los archivos originales NO se eliminan (operación de copia)
- Los archivos duplicados son sobrescritos en destino
- La aplicación es thread-safe para usos futuros
- Sin dependencias externas (Java 21 API)
