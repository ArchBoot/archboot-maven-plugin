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
  - [`hexagonal_modular`](#hexagonal_modular)
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

> [!NOTE]
> La arquitectura `hexagonal_modular` está disponible desde la versión **0.2.0**. Con `0.1.0` solo se acepta `mvc`.

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

| Parámetro           | Propiedad       | Tipo    | Obligatorio | Por defecto | Descripción                                                                                     |
|---------------------|-----------------|---------|-------------|-------------|-------------------------------------------------------------------------------------------------|
| `javaVersion`       | `javaVersion`   | String  | No          | `17`        | Versión de Java del proyecto generado.                                                          |
| `springVersion`     | `springVersion` | String  | No          | `4.0.0`     | Versión de Spring Boot del proyecto generado.                                                   |
| `configurationType` | `configuration` | String  | No          | `yml`       | Formato del archivo de configuración. Ver el [catálogo](#valores-del-archivo-de-configuración). |
| `gitKeep`           | `gitKeep`       | boolean | No          | `false`     | Crea archivos `.gitkeep` en los directorios vacíos.                                             |

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

| Valor (sin distinguir mayúsculas) | Arquitectura                   | Estado     | Detalle                                   |
|-----------------------------------|--------------------------------|------------|-------------------------------------------|
| `mvc`                             | Monolito en capas MVC          | Disponible | [`mvc`](#mvc)                             |
| `hexagonal_modular`               | Monolito hexagonal modular     | Disponible | [`hexagonal_modular`](#hexagonal_modular) |

### Valores del archivo de configuración

| Valor        | Archivo generado         |
|--------------|--------------------------|
| `yml`        | `application.yml`        |
| `properties` | `application.properties` |

## Qué hace el goal

1. Valida `architecture` y `configuration`.
2. Normaliza las entradas (ver [reglas](#reglas-de-normalización)).
3. Crea el directorio del proyecto `<output>/<artifactId>`.
4. Valida las versiones de Java y Spring Boot.
5. Escribe [`scaffold.yml`](#el-archivo-scaffoldyml).
6. Ejecuta el procesador de la arquitectura elegida, que genera la estructura, la clase principal, el archivo de configuración y los archivos base.

## Reglas de normalización

| Entrada                         | Regla                                                                                                                                                                                                     | Ejemplo                                                  |
|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------|
| `artifactId`                    | camelCase y separadores pasan a kebab-case, en minúsculas, y los caracteres inválidos pasan a `-`. Si queda vacío, usa `app`.                                                                             | `My Awesome App` → `my-awesome-app`                      |
| `name`                          | Si está en blanco, usa el `artifactId` tal como se indicó (antes de normalizar).                                                                                                                          | `MyApp` → `MyApp`                                        |
| `packageName`                   | Si está en blanco, se construye con `groupId.artifactId`: minúsculas, `-` pasa a `_`, las palabras reservadas de Java reciben `_` al final y los segmentos que empiezan con dígito reciben `_` al inicio. | `com.example` + `my-app` → `com.example.my_app`          |
| Clase principal                 | PascalCase del `artifactId` más el sufijo `Application`. Se antepone `App` si queda vacío o empieza con dígito.                                                                                           | `my-app` → `MyAppApplication`                            |
| `architecture`, `configuration` | Se pasan a mayúsculas, `-` y espacios pasan a `_`, y luego se comparan con el enum.                                                                                                                       | `mvc` → `MVC`, `hexagonal-modular` → `HEXAGONAL_MODULAR` |

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

| Archivo                              | Contenido                                                                                                                        |
|--------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| `pom.xml`                            | Parent de Spring Boot, dependencias (abajo), plugins de compilación y de Spring Boot.                                            |
| `<NombreClase>Application.java`      | Punto de entrada `@SpringBootApplication`. Carga un archivo `.env` (si existe) en las propiedades del sistema antes de arrancar. |
| `application.yml` / `.properties`    | Define `spring.application.name` con el `name` del proyecto.                                                                     |
| `.gitignore`, `.gitattributes`       | Reglas de ignorado para Maven, IDEs y `.env`; reglas de fin de línea para `mvnw` y `*.cmd`.                                      |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/…` | Maven Wrapper (Maven 3.9.10).                                                                                                    |
| `README.md`                          | Título con el `name` del proyecto.                                                                                               |
| `scaffold.yml`                       | Descripción de las entradas de generación.                                                                                       |
| `.gitkeep`                           | Solo con `gitKeep=true`, en cada directorio hoja vacío.                                                                          |

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

### `hexagonal_modular`

Arquitectura hexagonal (puertos y adaptadores) dividida en un proyecto Maven multimódulo. El `pom.xml` raíz es un agregador (`packaging: pom`) que declara tres módulos, cada uno con su propio `pom.xml`:

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
├── pom.xml                                # parent / agregador
├── scaffold.yml
├── domain/
│   ├── pom.xml
│   └── src/main/java/<ruta del paquete>/domain/
│       ├── model/
│       ├── gateway/
│       └── exception/
├── application/
│   ├── pom.xml
│   └── src/
│       ├── main/java/<ruta del paquete>/application/
│       │   ├── usecase/
│       │   └── handler/
│       └── test/java/<ruta del paquete>/application/
└── infrastructure/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/<ruta del paquete>/
        │   │   ├── <NombreClase>Application.java
        │   │   └── infrastructure/
        │   │       ├── config/
        │   │       ├── input/
        │   │       └── output/
        │   └── resources/
        │       └── application.yml        # o application.properties
        └── test/java/<ruta del paquete>/infrastructure/
```

| Módulo           | Artefacto                      | Depende de                      | Paquetes                          | Propósito                                                              |
|------------------|--------------------------------|---------------------------------|-----------------------------------|------------------------------------------------------------------------|
| `domain`         | `<artifactId>-domain`          | ninguno                         | `model`, `gateway`, `exception`   | Entidades de dominio, puertos (gateways) y excepciones de dominio.     |
| `application`    | `<artifactId>-application`     | `domain`                        | `usecase`, `handler`              | Casos de uso y handlers que orquestan el dominio.                      |
| `infrastructure` | `<artifactId>-infrastructure`  | `application`, `domain`         | `config`, `input`, `output`       | Adaptadores (REST, persistencia, etc.), configuración y arranque.      |

La regla de dependencia apunta hacia adentro: `infrastructure` → `application` → `domain`. El módulo `domain` no depende de Spring.

> [!NOTE]
> La clase principal (`<NombreClase>Application.java`) y el archivo de configuración viven en el módulo `infrastructure`, que es el único que reempaqueta el JAR ejecutable (`spring-boot-maven-plugin` se omite en el parent y se habilita con `repackage` en `infrastructure`). El módulo `domain` no tiene directorio `src/test`.

**Archivos generados**

| Archivo                              | Contenido                                                                                                                                               |
|--------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `pom.xml` (raíz)                     | Parent de Spring Boot, `<modules>`, `dependencyManagement` (módulos, springdoc, dotenv, Lombok, MapStruct) y `pluginManagement` de compilación y clean. |
| `domain/pom.xml`                     | Módulo `<artifactId>-domain`. Solo Lombok cuando `lombok=true`.                                                                                         |
| `application/pom.xml`                | Módulo `<artifactId>-application`. Depende de `domain`; ver dependencias abajo.                                                                         |
| `infrastructure/pom.xml`             | Módulo `<artifactId>-infrastructure`. Depende de `application` y `domain`; incluye la ejecución `repackage` de Spring Boot.                             |
| `<NombreClase>Application.java`      | Punto de entrada `@SpringBootApplication` en `infrastructure`. Carga un archivo `.env` (si existe) en las propiedades del sistema antes de arrancar.    |
| `application.yml` / `.properties`    | En `infrastructure/src/main/resources`. Define `spring.application.name` con el `name` del proyecto.                                                    |
| `.gitignore`, `.gitattributes`       | Igual que en `mvc`, generados en la raíz del proyecto.                                                                                                  |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/…` | Maven Wrapper (Maven 3.9.10), en la raíz del proyecto.                                                                                                  |
| `README.md`                          | Título con el `name` del proyecto.                                                                                                                      |
| `scaffold.yml`                       | Descripción de las entradas de generación (`architecture: HEXAGONAL_MODULAR`).                                                                          |
| `.gitkeep`                           | Solo con `gitKeep=true`, en cada directorio hoja vacío.                                                                                                 |

**Dependencias generadas por módulo**

| Dependencia                                 | `domain`      | `application`    | `infrastructure` |
|---------------------------------------------|---------------|------------------|------------------|
| `<artifactId>-domain`                       | n/a           | Siempre          | Siempre          |
| `<artifactId>-application`                  |               | n/a              | Siempre          |
| `spring-context` (provided)                 |               | Siempre          |                  |
| `spring-boot-starter-web`                   |               |                  | Siempre          |
| `spring-boot-starter-validation`            |               | Siempre          | Siempre          |
| `springdoc-openapi-starter-webmvc-ui`       |               |                  | Siempre          |
| `dotenv-java`                               |               |                  | Siempre          |
| `spring-boot-starter-test` (scope test)     |               |                  | Siempre          |
| `lombok`                                    | `lombok=true` | `lombok=true`    | `lombok=true`    |
| `mapstruct`                                 |               | `mapstruct=true` | `mapstruct=true` |

Las versiones (`springdoc` 2.5.0, `dotenv-java` 3.2.0, `lombok` 1.18.32, `mapstruct` 1.5.5.Final) se declaran una sola vez en el `pom.xml` raíz y los módulos las heredan.

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

Con `-Darchitecture=hexagonal_modular` la única diferencia es `architecture: HEXAGONAL_MODULAR`.

## Errores

| Mensaje                                      | Causa                                                                |
|----------------------------------------------|----------------------------------------------------------------------|
| `Invalid architecture type: <valor>`         | `architecture` no está en el [catálogo](#valores-de-arquitectura).   |
| `Invalid configuration type: <valor>`        | `configuration` no es `yml` ni `properties`.                         |
| `Output directory already exists: <ruta>`    | `<output>/<artifactId>` ya existe. No se sobrescribe nada.           |
| `Failed to create output directory: <ruta>`  | No se pudo crear el directorio (permisos, ruta inválida).            |
| `Java version not supported: <valor>`        | `javaVersion` es menor que 17.                                       |
| `Invalid Java version: <valor>`              | `javaVersion` no es un entero (por ejemplo `1.8`).                   |
| `Spring Boot version not supported: <valor>` | La versión mayor de Spring Boot es menor que 4.                      |
| `Invalid Spring Boot version: <valor>`       | `springVersion` no tiene formato numérico `mayor.menor[.parche]`.    |
| `Error creating project: <detalle>`          | No se pudo escribir `scaffold.yml`.                                  |

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

**Hexagonal modular**

```bash
mvn archboot:init \
    -DgroupId=com.example -DartifactId=orders-api -Darchitecture=hexagonal_modular \
    -Dlombok=true -Dmapstruct=true -DgitKeep=true
```