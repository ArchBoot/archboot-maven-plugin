# Changelog

**English** | [Español](CHANGELOG_ES.md)

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

---
## [0.1.0] - 2026-10-05

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
[0.1.0]: https://github.com/ArchBoot/archboot-maven-plugin/commits/0.1.0
