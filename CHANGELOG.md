# Changelog

**English** | [Español](CHANGELOG_ES.md)

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

---
## [0.2.0] - 2026-10-08
### Added
- New `hexagonal_modular` architecture for the `archboot:init` goal (`-Darchitecture=hexagonal_modular`, case-insensitive; `hexagonal-modular` is also accepted). It generates a multi-module Maven project:
    - `domain` (`model`, `gateway`, `exception`): no dependency on Spring.
    - `application` (`usecase`, `handler`): depends on `domain`.
    - `infrastructure` (`config`, `input`, `output`): depends on `application` and `domain`. It holds the main class and the `application.yml`/`application.properties`, and is the only module that repackages the executable JAR.
- Aggregator parent `pom.xml` (`packaging: pom`) with `dependencyManagement` for the modules, springdoc, dotenv-java, Lombok and MapStruct, and `pluginManagement` for compiler and clean.
- A `pom.xml` template for each module, with Lombok and MapStruct dependencies according to `lombok` and `mapstruct`.
- `ArchitectureType.HEXAGONAL_MODULAR`, registered in `ArchitectureProcessorFactory` with its `HexagonalModularArchitectureProcessor`.
- `ModuleLayout` and `ModuleDefinitionModel` to declaratively define the modules (suffix, dependencies, packages, tests, template).
- `PomGenerator.generateParent` and `generateModulePom`.
- Unit test for the `hexagonal_modular` happy path.

### Changed
- `ArchitectureProcessorAbstract` now resolves the main source directory and the resources directory per architecture (`resolveMainSourceDir`, `resolveResourcesDir`), so each architecture decides where they live.
- The generated `pom.xml` now declares `archboot-maven-plugin` at version `0.2.0`.

---
## [0.1.1] - 2026-10-06

### Fixed
- Fixed an issue in the `archboot:init` goal when generating the `scaffold.yml` descriptor. The `configuration` parameter was incorrectly serialized using the Java `Enum` representation, resulting in an invalid value containing the enum class path. The parameter is now correctly persisted as its corresponding configuration format string (for example, `yml` or `properties`).

---
## [0.1.0] - 2026-10-06

### Added

- `archboot:init` goal that generates a Spring Boot project skeleton. It does not require an existing Maven project.
- `mvc` architecture (controller, service, repository and model packages).
- Required parameters: `groupId`, `artifactId` and `architecture` (case-insensitive).
- Optional parameters: `version`, `description`, `name`, `packageName`, `javaVersion`, `springVersion`, `configuration`, `gitKeep`, `lombok`, `mapstruct` and `output`.
- Input normalization for `artifactId` (kebab-case), `packageName` (valid Java package) and the main class name (PascalCase + `Application`).
- Generated `pom.xml` with Spring Boot parent, `spring-boot-starter-web`, `spring-boot-starter-validation`, springdoc OpenAPI, dotenv-java and optional Lombok and MapStruct.
- Choice between `application.yml` and `application.properties`.
- Main class that loads a `.env` file into system properties on startup.
- Base files in the generated project: `.gitignore`, `.gitattributes`, `README.md` and Maven Wrapper (`mvnw`, `mvnw.cmd`, `maven-wrapper.properties`).
- Optional `.gitkeep` files in empty directories (`gitKeep=true`).
- `scaffold.yml` descriptor written to the root of the generated project.
- Documentation: bilingual README and detailed goal page for `init` (English and Spanish).
- Unit tests for the `init` goal.


[Unreleased]: https://github.com/ArchBoot/archboot-maven-plugin/commits/develop
[0.1.0]: https://github.com/ArchBoot/archboot-maven-plugin/commits/v0.1.0
[0.1.1]: https://github.com/ArchBoot/archboot-maven-plugin/compare/v0.1.0...v0.1.1
[0.2.0]: https://github.com/ArchBoot/archboot-maven-plugin/compare/v0.1.1...v0.2.0
