# Changelog

[English](CHANGELOG.md) | **Español**

Todos los cambios notables de este proyecto se documentan en este archivo.

El formato se basa en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y este proyecto sigue [Versionado Semántico](https://semver.org/lang/es/).

## [Unreleased]

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
[0.1.0]: https://github.com/ArchBoot/archboot-maven-plugin/commits/0.1.0