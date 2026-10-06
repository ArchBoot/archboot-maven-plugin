<div align="center">

# ArchBoot Maven Plugin

**Genera esqueletos de proyectos Spring Boot desde la línea de comandos, organizados por arquitectura.**

[![Maven Central](https://img.shields.io/maven-central/v/com.jagt.archboot.plugin/archboot-maven-plugin?logo=apachemaven&label=Maven%20Central)](https://central.sonatype.com/artifact/com.jagt.archboot.plugin/archboot-maven-plugin)
[![GitHub Tag](https://img.shields.io/github/v/tag/ArchBoot/archboot-maven-plugin?sort=semver&logo=git&label=Version)](https://github.com/ArchBoot/archboot-maven-plugin/tags)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![CI](https://github.com/ArchBoot/archboot-maven-plugin/actions/workflows/ci-app.yml/badge.svg?branch=main)](https://github.com/ArchBoot/archboot-maven-plugin/actions/workflows/ci-app.yml)

[English](README.md) | **Español**

</div>

---

## Tabla de contenido

- [Acerca de](#acerca-de)
- [Coordenadas del plugin](#coordenadas-del-plugin)
- [Requisitos](#requisitos)
- [Uso](#uso)
    - [Opción A: desde Maven Central](#opción-a-desde-maven-central)
    - [Opción B: clonar y compilar desde el código fuente](#opción-b-clonar-y-compilar-desde-el-código-fuente)
- [Goals](#goals)
- [Ejemplo rápido](#ejemplo-rápido)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

## Acerca de

ArchBoot es un plugin de Maven que crea un proyecto Spring Boot listo para ejecutar con un solo comando. Tú eliges la arquitectura y el plugin genera el `pom.xml`, la estructura de paquetes, la clase principal, el archivo de configuración, el Maven Wrapper y los archivos base de Git, además de un `scaffold.yml` que describe lo que se generó.

**No** necesita un proyecto Maven existente, por lo que puede ejecutarse desde cualquier directorio.

## Coordenadas del plugin

```xml
<groupId>com.jagt.archboot.plugin</groupId>
<artifactId>archboot-maven-plugin</artifactId>
<version>0.1.0</version>
```

| Propiedad          | Valor                                                        |
|--------------------|--------------------------------------------------------------|
| Empaquetado        | `maven-plugin`                                               |
| Prefijo del goal   | `archboot`                                                   |
| Licencia           | [Apache License 2.0](LICENSE)                                |
| Java               | 17+                                                          |
| Maven              | 3.9+                                                         |
| SCM                | https://github.com/ArchBoot/archboot-maven-plugin            |
| Autor              | [Jhon Alexander Gomez Trujillo](https://github.com/JAGT1806) |

## Requisitos

- JDK 17 o superior
- Maven 3.9 o superior (o el Maven Wrapper incluido si trabajas desde el código fuente)
- Git (solo para la Opción B)

## Uso

Hay dos formas de ejecutar el plugin.

### Opción A: desde Maven Central

No requiere instalación. Maven descarga el plugin la primera vez que se usa.

```bash
mvn com.jagt.archboot.plugin:archboot-maven-plugin:0.1.0:init \
    -DgroupId=com.example \
    -DartifactId=my-app \
    -Darchitecture=mvc
```

**Prefijo corto (opcional).** Registra el grupo del plugin en `~/.m2/settings.xml` para usar `archboot:init` en lugar de las coordenadas completas:

```xml
<settings>
  <pluginGroups>
    <pluginGroup>com.jagt.archboot.plugin</pluginGroup>
  </pluginGroups>
</settings>
```

```bash
mvn archboot:init -DgroupId=com.example -DartifactId=my-app -Darchitecture=mvc
```

### Opción B: clonar y compilar desde el código fuente

Úsala para probar cambios no publicados o para contribuir.

**1. Clonar el repositorio**

```bash
git clone https://github.com/ArchBoot/archboot-maven-plugin.git
cd archboot-maven-plugin
```

**2. Compilar e instalar el plugin en tu repositorio local**

```bash
./mvnw clean install        # Linux / macOS
mvnw.cmd clean install      # Windows
```

**3. Ejecutar el goal** con la versión que acabas de instalar (`0.1.0-SNAPSHOT`):

```bash
mvn com.jagt.archboot.plugin:archboot-maven-plugin:0.1.0-SNAPSHOT:init \
    -DgroupId=com.example \
    -DartifactId=my-app \
    -Darchitecture=mvc \
    -Doutput=/ruta/al/workspace
```

> [!NOTE]
> El `pom.xml` generado declara `archboot-maven-plugin` en la versión `0.1.0`. Esa versión debe poder resolverse (desde Maven Central o desde tu repositorio local) al compilar el proyecto generado.

## Goals

| Goal                                       | Descripción                                                          | Requiere proyecto Maven | Documentación                                                    |
|--------------------------------------------|----------------------------------------------------------------------|-------------------------|------------------------------------------------------------------|
| [`archboot:init`](docs/goals/init.es.md)   | Genera el esqueleto de un proyecto Spring Boot para una arquitectura | No                      | [English](docs/goals/init.md) · [Español](docs/goals/init.es.md) |

Cada página de goal describe sus parámetros, el catálogo de valores aceptados y la estructura de proyecto que se genera para cada arquitectura.

## Ejemplo rápido

```bash
mvn archboot:init \
    -DgroupId=com.example \
    -DartifactId=my-app \
    -Darchitecture=mvc \
    -Dlombok=true \
    -Dmapstruct=true
```

Resultado:

```text
my-app/
├── pom.xml
├── scaffold.yml
├── mvnw / mvnw.cmd
└── src/
    ├── main/java/com/example/my_app/
    │   ├── MyAppApplication.java
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   └── model/
    └── main/resources/application.yml
```

## Contribuir

1. Haz un fork del repositorio y crea una rama desde `develop` (`feature/*`, `bugfix/*`, `hotfix/*`).
2. Ejecuta `./mvnw clean verify` antes de abrir un pull request.
3. Abre el pull request hacia `develop`.

## Licencia

Distribuido bajo la [Apache License 2.0](LICENSE).