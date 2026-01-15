## Ejemplos de Uso - Photo Organizer

### Ejemplo 1: Uso Básico

**Configurar config.properties:**
```properties
source.directory=/home/user/Descargas/fotos_sin_ordenar
destination.directory=/home/user/Fotos/organizadas
folder.structure=YYYY/MM
```

**Ejecutar:**
```bash
java -jar photo-organizer.jar
```

**Resultado esperado:**
```
Configuración cargada correctamente:
  Origen: /home/user/Descargas/fotos_sin_ordenar
  Destino: /home/user/Fotos/organizadas
  Estructura: YYYY/MM

========== Iniciando organización de fotos ==========

Se encontraron 142 fotos para organizar.

[OK] vacation_1.jpg -> 2024/07
[OK] vacation_2.jpg -> 2024/07
...
========== Resumen del proceso ==========
Total de fotos encontradas: 142
Fotos procesadas correctamente: 142
Errores encontrados: 0
```

---

### Ejemplo 2: Organismos con Estructuras Anidadas

Si tienes fotos en múltiples subcarpetas:
```
fotos_sin_ordenar/
├── iphone/
│   ├── selfies/
│   │   └── photo.jpg
│   └── landscapes/
│       └── mountain.jpg
├── dslr/
│   ├── 2024-06-15/
│   │   └── model_shots.jpg
│   └── 2024-07-20/
│       └── nature.jpg
└── scans/
    └── old_photos.jpg
```

La aplicación busca **recursivamente** en todas las subcarpetas y organiza por año/mes:
```
organizadas/
├── 2024/
│   ├── 06/
│   │   ├── model_shots.jpg
│   │   └── mountain.jpg
│   └── 07/
│       └── landscape.jpg
└── 2023/
    └── 12/
        └── old_photos.jpg
```

---

### Ejemplo 3: Diferentes Formatos de Imagen

La aplicación soporta automáticamente:

```
fotos/
├── photo.jpg          ✓ Soportado
├── image.jpeg         ✓ Soportado
├── picture.png        ✓ Soportado
├── animation.gif      ✓ Soportado
├── bitmap.bmp         ✓ Soportado
├── modern.webp        ✓ Soportado
├── archive.tiff       ✓ Soportado
├── raw_image.raw      ✓ Soportado
├── document.pdf       ✗ Ignorado
└── video.mp4          ✗ Ignorado
```

---

### Ejemplo 4: Manejo de Errores

Si encuentras problemas:

**Error: Directorio de origen no existe**
```
Error: El directorio de origen no existe: /home/user/missing_folder
```
Solución: Verifica que la ruta exista y sea correcta

**Error: Permisos insuficientes**
```
[ERROR] photo.jpg: Permission denied
```
Solución: Asegúrate de tener permiso de escritura en el destino

**Error: Configuración incompleta**
```
Error al cargar la configuración: source.directory no está configurado
```
Solución: Edita config.properties y añade las rutas necesarias

---

### Ejemplo 5: Uso Batch (múltiples ejecuciones)

Script para organizar fotos periódicamente:

```bash
#!/bin/bash
# organize_photos.sh

ORGANIZER="/home/user/photo-organizer.jar"
LOG="/var/log/photo_organizer.log"

echo "$(date): Iniciando organización..." >> $LOG

java -jar $ORGANIZER >> $LOG 2>&1

if [ $? -eq 0 ]; then
    echo "$(date): Organización completada exitosamente" >> $LOG
else
    echo "$(date): Error en la organización" >> $LOG
    exit 1
fi
```

Usar con cron para automatizar:
```bash
# Ejecutar cada domingo a las 3 AM
0 3 * * 0 /home/user/organize_photos.sh
```

---

### Ejemplo 6: Configuración para Diferentes Usuarios

**Equipo de fotografía 1 (Fotografía profesional):**
```properties
source.directory=/media/storage/raw_photos
destination.directory=/media/storage/organized_shoots
folder.structure=YYYY/MM
```

**Equipo de fotografía 2 (Fotos personales):**
```properties
source.directory=/home/photographer/incoming
destination.directory=/home/photographer/Photos/archive
folder.structure=YYYY/MM
```

**Servidor de backup:**
```properties
source.directory=/mnt/network/photos/pending
destination.directory=/mnt/backup/photos
folder.structure=YYYY/MM
```

---

### Ejemplo 7: Flujo Completo de Organización

1. **Preparación:**
   ```bash
   # Crear directorios si no existen
   mkdir -p ~/Fotos/incoming ~/Fotos/organized
   ```

2. **Copiar fotos sin organizar:**
   ```bash
   cp ~/Descargas/*.jpg ~/Fotos/incoming/
   cp ~/Descargas/*.png ~/Fotos/incoming/
   ```

3. **Configurar aplicación:**
   ```bash
   # Editar src/main/resources/config.properties
   source.directory=/home/user/Fotos/incoming
   destination.directory=/home/user/Fotos/organized
   ```

4. **Compilar:**
   ```bash
   ./build.sh
   ```

5. **Ejecutar:**
   ```bash
   java -jar photo-organizer.jar
   ```

6. **Verificar resultado:**
   ```bash
   tree ~/Fotos/organized/
   ```

7. **Limpiar (opcional):**
   ```bash
   rm -rf ~/Fotos/incoming/*.jpg ~/Fotos/incoming/*.png
   ```

---

### Ejemplo 8: Monitoreo y Reporte

Para guardar un reporte:

```bash
java -jar photo-organizer.jar > report_$(date +%Y%m%d_%H%M%S).txt 2>&1
```

Ejemplo de reporte guardado:
```
╔════════════════════════════════════╗
║     Photo Organizer v1.0          ║
║  Organiza fotos por año y mes      ║
╚════════════════════════════════════╝

Configuración cargada correctamente:
  Origen: /home/user/Fotos/incoming
  Destino: /home/user/Fotos/organized
  Estructura: YYYY/MM

========== Iniciando organización de fotos ==========

Se encontraron 287 fotos para organizar.

[OK] IMG_001.jpg -> 2024/12
[OK] IMG_002.jpg -> 2024/12
...
[ERROR] corrupted_file.jpg: Permission denied
...

========== Resumen del proceso ==========
Total de fotos encontradas: 287
Fotos procesadas correctamente: 286
Errores encontrados: 1
========================================

¡Proceso completado!
```

---

### Ejemplo 9: Integración con Sistema de Archivos

Crear alias para fácil acceso:

```bash
# En ~/.bashrc o ~/.zshrc
alias organize-photos='java -jar ~/photo-organizer.jar'
```

Luego simplemente:
```bash
organize-photos
```

---

### Ejemplo 10: Verificación Previa

Script para verificar configuración antes de ejecutar:

```bash
#!/bin/bash

SOURCE=$(grep "source.directory" config.properties | cut -d'=' -f2)
DEST=$(grep "destination.directory" config.properties | cut -d'=' -f2)

echo "Verificando configuración..."
echo "Origen: $SOURCE"
echo "Destino: $DEST"

if [ ! -d "$SOURCE" ]; then
    echo "❌ Directorio origen no existe"
    exit 1
fi

echo "✓ Directorio origen existe"
echo "✓ Se encontraron $(find $SOURCE -type f | wc -l) archivos"

if [ ! -d "$DEST" ]; then
    echo "⚠ Creando directorio destino..."
    mkdir -p "$DEST"
fi

echo "✓ Listo para ejecutar"
```

---

### Troubleshooting

**P: ¿Por qué algunas fotos no se organizaron?**
R: Probablemente sean archivos sin formato soportado. Revisa los formatos en el archivo README.

**P: ¿Se eliminan los archivos originales?**
R: No, se **copian**. Los originales permanecen en el directorio fuente.

**P: ¿Qué pasa con archivos duplicados?**
R: Se **sobrescriben** con la última copia. Considera renombrar fotos si quieres mantener múltiples versiones.

**P: ¿Cómo cambio la estructura de carpetas?**
R: Edita `config.properties` y modifica `folder.structure`. Por ahora soporta `YYYY/MM`.

**P: ¿Puedo ejecutar la aplicación en segundo plano?**
R: Sí: `java -jar photo-organizer.jar &` o usa `nohup`

---

### Mejores Prácticas

1. **Hacer backup** antes de organizar fotos valiosas
2. **Probar** con una carpeta pequeña primero
3. **Monitorear logs** de ejecución
4. **Programar** ejecuciones automáticas con cron
5. **Verificar** resultados antes de eliminar originales
6. **Usar directorios separados** para diferentes fuentes
7. **Mantener actualizado** el archivo de configuración
