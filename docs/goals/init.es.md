# Goal `archboot:init`

[← Volver al README](../../README_ES.md) · [English](init.md) | **Español**

Genera el esqueleto de un nuevo proyecto Spring Boot para la arquitectura elegida.

| Atributo          | Valor                                                 |
|-------------------|-------------------------------------------------------|
| Nombre completo   | `top.jagt.archboot.plugin:archboot-maven-plugin:init` |
| Nombre corto      | `archboot:init`                                       |
| Desde             | 0.1.0                                                 |
| Requiere proyecto | No (`requiresProject = false`)                        |
| Implementación    | `top.jagt.archboot.plugin.mojo.InitProjectMojo`       |

## Tabla de contenido

- [Uso](#uso)
- [Parámetros](#parámetros)
- [Catálogo](#catálogo)
- [Qué hace el goal](#qué-hace-el-goal)
- [Reglas de normalización](#reglas-de-normalización)
- [Arquitecturas](#arquitecturas)
    - [`mvc`](#mvc)
- [El archivo `scaffold.yml`](#el-archivo-scaffoldyml)
- [Errores](#errores)
- [Ejemplos](#ejemplos)

## Uso

```bash
mvn top.jagt.archboot.plugin:archboot-maven-plugin:0.1.0:init \
    -DgroupId=<groupId> \
    -DartifactId=<artifactId> \
    -Darchitecture=<architecture> \
    [otros parámetros]
```

`groupId`, `artifactId` y `architecture` son obligatorios. El resto tiene valor por defecto.

## Parámetros

Los parámetros se pasan con `-D<propiedad>=<valor>`.

### Valores principales

| Parámetro      | Propiedad      | Tipo   | Obligatorio | Por defecto        | Descripción                                                                                   |
|----------------|----------------|--------|-------------|--------------------|-----------------------------------------------------------------------------------------------|
| `groupId`      | `groupId`      | String | **Sí**      |                    | `groupId` Maven del proyecto generado.                                                        |
| `artifactId`   | `artifactId`   | String | **Sí**      |                    | `artifactId` Maven del proyecto generado. Se normaliza a kebab-case.                          |
| `architecture` | `architecture` | String | **Sí**      |                    | Arquitectura a generar. No distingue mayúsculas. Ver el [catálogo](#valores-de-arquitectura). |
| `version`      | `version`      | String | No          | `0.1.0-SNAPSHOT`   | Versión del proyecto generado.                                                                |
| `description`  | `description`  | String | No          |                    | Descripción corta del proyecto generado.                                                      |

### Nombres

| Parámetro     | Propiedad     | Tipo   | Obligatorio | Por defecto                                             | Descripción                      |
|---------------|---------------|--------|-------------|---------------------------------------------------------|----------------------------------|
| `name`        | `name`        | String | No          | el `artifactId` tal como se indicó                      | Nombre legible del proyecto.     |
| `packageName` | `packageName` | String | No          | `groupId` + `.` + `artifactId` normalizado, normalizado | Paquete Java base.               |

### Configuración

| Parámetro           | Propiedad       | Tipo    | Obligatorio | Por defecto | Descripción                                                         |
|---------------------|-----------------|---------|-------------|-------------|-------------- -------------------------------------------------------|
| `javaVersion`       | `javaVersion`   | String  | No          | `17`        | Versión de Java del proyecto generado.                              |
| `springVersion`     | `springVersion` | String  | No          | `4.0.0`     | Versión de Spring Boot del proyecto generado.                       |
| `configurationType` | `configuration` | String  | No          | `yml`       | Formato del archivo de configuración. Ver el [catálogo](#valores-del-archivo-de-configuración). |
| `gitKeep`           | `gitKeep`       | boolean | No          | `false`     | Crea archivos `.gitkeep` en los directorios vacíos.                 |

> [!NOTE]
> `configurationType` se define con la propiedad **`configuration`**: `-Dconfiguration=properties`.

### Procesadores de anotaciones

| Parámetro   | Propiedad   | Tipo    | Obligatorio | Por defecto | Descripción                                 |
|-------------|-------------|---------|-------------|-------------|---------------------------------------------|
| `lombok`    | `lombok`    | boolean | No          | `false`     | Agrega Lombok al proyecto generado.         |
| `mapstruct` | `mapstruct` | boolean | No          | `false`     | Agrega MapStruct al proyecto generado.      |

Si ambos están activos, el `pom.xml` generado agrega también `lombok-mapstruct-binding` para que los dos procesadores funcionen juntos.

### Salida

| Parámetro   | Propiedad | Tipo | Obligatorio | Por defecto                     | Descripción                                                         |
|-------------|-----------|------|-------------|---------------------------------|---------------------------------------------------------------------|
| `outputDir` | `output`  | File | No          | directorio de trabajo actual    | Directorio donde se crea la carpeta del proyecto (`<artifactId>`).  |

> [!NOTE]
> `outputDir` se define con la propiedad **`output`**: `-Doutput=/ruta/al/workspace`.

## Catálogo

### Valores de arquitectura

| Valor (sin distinguir mayúsculas) | Arquitectura                  | Estado     | Detalle        |
|-----------------------------------|-------------------------------|------------|----------------|
| `mvc`                             | Monolito en capas MVC         | Disponible | [`mvc`](#mvc)  |

### Valores del archivo de configuración

| Valor        | Archivo generado         |
|--------------|--------------------------|
| `yml`        | `application.yml`        |
| `properties` | `application.properties` |

## Qué hace el goal

1. Valida `architecture` y `configuration`.
2. Normaliza las entradas (ver [reglas](#reglas-de-normalización)).
3. Crea el directorio del proyecto `<output>/<artifactId>`.
4. Escribe [`scaffold.yml`](#el-archivo-scaffoldyml).
5. Ejecuta el procesador de la arquitectura elegida, que genera la estructura, la clase principal, el archivo de configuración y los archivos base.

## Reglas de normalización

| Entrada       | Regla                                                                                                             | Ejemplo                                    |
|---------------|-------------------------------------------------------------------------------------------------------------------|--------------------------------------------|
| `artifactId`  | camelCase y separadores pasan a kebab-case, en minúsculas, y los caracteres inválidos pasan a `-`. Si queda vacío, usa `app`. | `My Awesome App` → `my-awesome-app` |
| `name`        | Si está en blanco, usa el `artifactId` tal como se indicó (antes de normalizar).                                  | `MyApp` → `MyApp`                          |
| `packageName` | Si está en blanco, se construye con `groupId.artifactId`: minúsculas, `-` pasa a `_`, las palabras reservadas de Java reciben `_` al final y los segmentos que empiezan con dígito reciben `_` al inicio. | `com.example` + `my-app` → `com.example.my_app` |
| Clase principal | PascalCase del `artifactId` más el sufijo `Application`. Se antepone `App` si queda vacío o empieza con dígito. | `my-app` → `MyAppApplication`            |
| `architecture`, `configuration` | Se pasan a mayúsculas, `-` y espacios pasan a `_`, y luego se comparan con el enum.             | `mvc` → `MVC`                              |

Un `packageName` explícito se conserva tal cual.

## Arquitecturas

### `mvc`

Monolito en capas tradicional: controller → service → repository → model.

```text
<artifactId>/
├── .gitattributes
├── .gitignore
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── scaffold.yml
└── src/
    ├── main/
    │   ├── java/<ruta del paquete>/
    │   │   ├── <NombreClase>Application.java
    │   │   ├── controller/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   └── service/
    │   └── resources/
    │       └── application.yml            # o application.properties
    └── test/
        └── java/
```

| Paquete      | Propósito                                    |
|--------------|----------------------------------------------|
| `controller` | Capa HTTP (controladores REST).              |
| `service`    | Lógica de negocio.                           |
| `repository` | Acceso a datos.                              |
| `model`      | Entidades y modelos de dominio.              |

**Archivos generados**

| Archivo                                | Contenido                                                                                 |
|----------------------------------------|-------------------------------------------------------------------------------------------|
| `pom.xml`                              | Parent de Spring Boot, dependencias (abajo), plugins de compilación y de Spring Boot.     |
| `<NombreClase>Application.java`        | Punto de entrada `@SpringBootApplication`. Carga un archivo `.env` (si existe) en las propiedades del sistema antes de arrancar. |
| `application.yml` / `.properties`      | Define `spring.application.name` con el `name` del proyecto.                              |
| `.gitignore`, `.gitattributes`         | Reglas de ignorado para Maven, IDEs y `.env`; reglas de fin de línea para `mvnw` y `*.cmd`. |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/…`   | Maven Wrapper (Maven 3.9.10).                                                             |
| `README.md`                            | Título con el `name` del proyecto.                                                        |
| `scaffold.yml`                         | Descripción de las entradas de generación.                                                |
| `.gitkeep`                             | Solo con `gitKeep=true`, en cada directorio hoja vacío.                                   |

**Dependencias generadas**

| Dependencia                                  | Condición        |
|----------------------------------------------|------------------|
| `spring-boot-starter-web`                    | Siempre          |
| `spring-boot-starter-validation`             | Siempre          |
| `springdoc-openapi-starter-webmvc-ui` 2.5.0  | Siempre          |
| `dotenv-java` 3.2.0                          | Siempre          |
| `spring-boot-starter-test` (scope test)      | Siempre          |
| `lombok` 1.18.32 (provided)                  | `lombok=true`    |
| `mapstruct` 1.5.5.Final                      | `mapstruct=true` |

## El archivo `scaffold.yml`

Se escribe en la raíz del proyecto antes de ejecutar el procesador de la arquitectura. Ejemplo con `-Dlombok=true -Dmapstruct=true`:

```yaml
scaffold:
  architecture: MVC
  data:
    artifactId: my-app
    groupId: com.example
    name: my-app
    packageName: com.example.my_app
    annotations:
      lombok: true
      mapstruct: true
    config:
      config:
        extension: YML
```

## Errores

| Mensaje                                         | Causa                                                       |
|-------------------------------------------------|-------------------------------------------------------------|
| `Invalid architecture type: <valor>`            | `architecture` no está en el [catálogo](#valores-de-arquitectura). |
| `Invalid configuration type: <valor>`           | `configuration` no es `yml` ni `properties`.                |
| `Output directory already exists: <ruta>`       | `<output>/<artifactId>` ya existe. No se sobrescribe nada.  |
| `Failed to create output directory: <ruta>`     | No se pudo crear el directorio (permisos, ruta inválida).   |
| `Error creating project: <detalle>`             | No se pudo escribir `scaffold.yml`.                         |

## Ejemplos

**Mínimo**

```bash
mvn archboot:init -DgroupId=com.example -DartifactId=my-app -Darchitecture=mvc
```

**Con `application.properties`, Java 21 y archivos `.gitkeep`**

```bash
mvn archboot:init \
    -DgroupId=com.example -DartifactId=orders-api -Darchitecture=mvc \
    -Dconfiguration=properties -DjavaVersion=21 -DgitKeep=true
```

**Nombre, paquete y directorio de salida personalizados**

```bash
mvn archboot:init \
    -DgroupId=com.acme -DartifactId=billing -Darchitecture=MVC \
    -Dname="Billing Service" -DpackageName=com.acme.billing \
    -Dversion=1.0.0-SNAPSHOT -Dlombok=true -Dmapstruct=true \
    -Doutput=$HOME/workspace
```