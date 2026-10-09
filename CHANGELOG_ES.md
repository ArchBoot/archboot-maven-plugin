# Changelog

[English](CHANGELOG.md) | **Español**

Todos los cambios notables de este proyecto se documentan en este archivo.

El formato se basa en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y este proyecto sigue [Versionado Semántico](https://semver.org/lang/es/).

## [Unreleased]

---
## [0.2.0] - 2026-10-08

### Añadido
- Nueva arquitectura `hexagonal_modular` para el goal `archboot:init` (`-Darchitecture=hexagonal_modular`, sin distinguir mayúsculas; también se acepta `hexagonal-modular`). Genera un proyecto Maven multimódulo:
    - `domain` (`model`, `gateway`, `exception`): sin dependencia de Spring.
    - `application` (`usecase`, `handler`): depende de `domain`.
    - `infrastructure` (`config`, `input`, `output`): depende de `application` y `domain`. Contiene la clase principal y el `application.yml`/`application.properties`, y es el único módulo que reempaqueta el JAR ejecutable.
- `pom.xml` padre agregador (`packaging: pom`) con `dependencyManagement` de los módulos, springdoc, dotenv-java, Lombok y MapStruct, y `pluginManagement` de compilación y clean.
- Plantilla `pom.xml` por módulo, con dependencias de Lombok y MapStruct según `lombok` y `mapstruct`.
- `ArchitectureType.HEXAGONAL_MODULAR`, registrado en `ArchitectureProcessorFactory` con su `HexagonalModularArchitectureProcessor`.
- `ModuleLayout` y `ModuleDefinitionModel` para definir los módulos de forma declarativa (sufijo, dependencias, paquetes, pruebas y plantilla).
- `PomGenerator.generateParent` y `generateModulePom`.
- Prueba unitaria del caso exitoso de `hexagonal_modular`.

### Cambiado
- `ArchitectureProcessorAbstract` ahora resuelve por arquitectura el directorio de código fuente principal y el de recursos (`resolveMainSourceDir`, `resolveResourcesDir`), de modo que cada arquitectura decide dónde viven.
- El `pom.xml` generado ahora declara `archboot-maven-plugin` en la versión `0.2.0`.

---
## [0.1.1] - 2026-10-06

### Corregido
- Se ha corregido un problema en el objetivo `archboot:init` al generar el descriptor `scaffold.yml`. El parámetro `configuration` se serializaba incorrectamente utilizando la representación `Enum` de Java, lo que daba lugar a un valor no válido que incluía la ruta de la clase del enum. Ahora, el parámetro se guarda correctamente como la cadena de texto correspondiente a su formato de configuración (por ejemplo, `yml` o `properties`).

---
## [0.1.0] - 2026-10-06

### Añadido

- Goal `archboot:init` que genera el esqueleto de un proyecto Spring Boot. No requiere un proyecto Maven existente.
- Arquitectura `mvc` (paquetes controller, service, repository y model).
- Parámetros obligatorios: `groupId`, `artifactId` y `architecture` (sin distinguir mayúsculas).
- Parámetros opcionales: `version`, `description`, `name`, `packageName`, `javaVersion`, `springVersion`, `configuration`, `gitKeep`, `lombok`, `mapstruct` y `output`.
- Normalización de entradas para `artifactId` (kebab-case), `packageName` (paquete Java válido) y el nombre de la clase principal (PascalCase + `Application`).
- `pom.xml` generado con parent de Spring Boot, `spring-boot-starter-web`, `spring-boot-starter-validation`, springdoc OpenAPI, dotenv-java y, opcionalmente, Lombok y MapStruct.
- Elección entre `application.yml` y `application.properties`.
- Clase principal que carga un archivo `.env` en las propiedades del sistema al arrancar.
- Archivos base en el proyecto generado: `.gitignore`, `.gitattributes`, `README.md` y Maven Wrapper (`mvnw`, `mvnw.cmd`, `maven-wrapper.properties`).
- Archivos `.gitkeep` opcionales en directorios vacíos (`gitKeep=true`).
- Descriptor `scaffold.yml` escrito en la raíz del proyecto generado.
- Documentación: README bilingüe y página detallada del goal `init` (inglés y español).
- Pruebas unitarias del goal `init`.

<!--
Cuando hagas el primer release, renombra [Unreleased] a [0.1.0] - AAAA-MM-DD
y agrega arriba una nueva sección [Unreleased] vacía.
-->

[Unreleased]: https://github.com/ArchBoot/archboot-maven-plugin/commits/develop
[0.1.0]: https://github.com/ArchBoot/archboot-maven-plugin/commits/v0.1.0
[0.1.1]: https://github.com/ArchBoot/archboot-maven-plugin/compare/v0.1.0...v0.1.1
[0.2.0]: https://github.com/ArchBoot/archboot-maven-plugin/compare/v0.1.1...v0.2.0
