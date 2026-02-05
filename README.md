# Photo Organizer 📸

Una aplicación Java para organizar automáticamente fotos basándose en sus metadatos (fecha de captura), creando una estructura de carpetas por año/mes.

## Características

✅ **Java 21** - Sin frameworks externos pesados, solo APIs estándar de Java y una librería ligera para EXIF  
✅ **Configuración flexible** - Archivo `config.properties` para definir rutas  
✅ **Organización automática** - Agrupa fotos por año/mes: `YYYY/MM`  
✅ **Metadatos EXIF** - Usa la fecha de captura (EXIF) cuando está disponible y, si no, la fecha de modificación del archivo  
✅ **Múltiples formatos** - Soporta JPG, PNG, GIF, BMP, WebP, TIFF, RAW  
✅ **Búsqueda recursiva** - Encuentra fotos en subdirectorios  

## Estructura del Proyecto

```
photo-organizer-java/
├── src/main/java/com/photoorganizer/
│   ├── Main.java              # Punto de entrada de la aplicación
│   ├── Config.java            # Gestor de configuración
│   ├── PhotoMetadata.java     # Extracción de metadatos
│   └── PhotoOrganizer.java    # Lógica principal de organización
├── src/main/resources/
│   └── config.properties      # Archivo de configuración
├── pom.xml                    # Configuración Maven
└── build.sh                   # Script de compilación
```

## Configuración

Edita `src/main/resources/config.properties`:

```properties
# Ruta de origen (donde están las fotos sin organizar)
source.directory=/ruta/a/fotos/entrada

# Ruta de destino (donde se guardarán organizadas)
destination.directory=/ruta/a/fotos/salida

# Estructura de carpetas (YYYY/MM es el formato por defecto)
folder.structure=YYYY/MM
```

### Ejemplos de configuración:

**Linux/Mac:**
```properties
source.directory=/home/usuario/Descargas/fotos
destination.directory=/home/usuario/Fotos/organizadas
```

**Windows:**
```properties
source.directory=C:/Users/Usuario/Downloads/fotos
destination.directory=C:/Users/Usuario/Pictures/organizadas
```

## Compilación

### Opción 1: Con Maven (recomendado)

```bash
mvn clean package
```

El JAR se generará en `target/photo-organizer.jar`

### Opción 2: Usar el script de compilación

```bash
chmod +x build.sh
./build.sh
```

### Opción 3: Compilación manual

```bash
mkdir -p build
javac -d build -encoding UTF-8 src/main/java/com/photoorganizer/*.java
cp src/main/resources/config.properties build/
cd build
jar cfe ../photo-organizer.jar com.photoorganizer.Main com/photoorganizer/*.class config.properties
```

## Uso

### Con Maven:
```bash
mvn exec:java -Dexec.mainClass="com.photoorganizer.Main"
```

### Con el JAR compilado:
```bash
java -jar photo-organizer.jar
```

O directamente con Java 21:
```bash
java -cp target/photo-organizer.jar com.photoorganizer.Main
```

## Ejemplo de Ejecución

```
╔════════════════════════════════════╗
║     Photo Organizer v1.0          ║
║  Organiza fotos por año y mes      ║
╚════════════════════════════════════╝

Configuración cargada correctamente:
  Origen: /home/usuario/Descargas/fotos
  Destino: /home/usuario/Fotos/organizadas
  Estructura: YYYY/MM

========== Iniciando organización de fotos ==========

Se encontraron 42 fotos para organizar.

[OK] foto001.jpg -> 2024/12
[OK] foto002.jpg -> 2024/12
[OK] vacaciones.png -> 2023/07
[OK] boda.jpg -> 2022/06
...

========== Resumen del proceso ==========
Total de fotos encontradas: 42
Fotos procesadas correctamente: 42
Errores encontrados: 0
========================================
```

## Estructura de Carpetas Generada

Después de ejecutar, tus fotos se organizarán así:

```
organizadas/
├── 2024/
│   ├── 01/
│   │   ├── foto_enero_1.jpg
│   │   └── foto_enero_2.jpg
│   ├── 06/
│   │   └── viaje_verano.jpg
│   └── 12/
│       ├── navidad_1.jpg
│       └── navidad_2.jpg
├── 2023/
│   ├── 07/
│   │   ├── vacaciones_1.jpg
│   │   └── vacaciones_2.jpg
│   └── 12/
│       └── ano_nuevo.jpg
└── 2022/
    └── 06/
        └── boda.jpg
```

## Formatos Soportados

- JPEG/JPG (*.jpg, *.jpeg)
- PNG (*.png)
- GIF (*.gif)
- BMP (*.bmp)
- WebP (*.webp)
- TIFF (*.tiff)
- RAW (*.raw)

## Requisitos

- **Java 21** o superior
- **Maven 3.6+** (opcional, solo si usas maven para compilar)

## Notas de Seguridad

- La aplicación **copia** los archivos originales sin eliminarlos
- Los archivos duplicados se sobrescriben en el destino
- Se recomienda hacer backup antes de usar en fotos valiosas

## Mejoras Futuras Posibles

- Renombrado de archivos basado en metadatos
- Filtrado por rango de fechas
- Logging detallado
- Interfaz gráfica

## Licencia

Código abierto - Libre para usar y modificar

## Autor

Desarrollado como solución para organizar fotos de manera automática y eficiente.
