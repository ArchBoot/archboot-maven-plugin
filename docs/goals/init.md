# Goal `archboot:init`

[← Back to README](../../README.md) · **English** | [Español](init.es.md)

Generates a new Spring Boot project skeleton for the chosen architecture.

| Attribute          | Value                                                 |
|--------------------|-------------------------------------------------------|
| Full name          | `top.jagt.archboot.plugin:archboot-maven-plugin:init` |
| Short name         | `archboot:init`                                       |
| Since              | 0.1.0                                                 |
| Requires a project | No (`requiresProject = false`)                        |
| Implementation     | `top.jagt.archboot.plugin.mojo.InitProjectMojo`       |

## Table of contents

- [Usage](#usage)
- [Parameters](#parameters)
- [Catalog](#catalog)
- [What the goal does](#what-the-goal-does)
- [Normalization rules](#normalization-rules)
- [Architectures](#architectures)
  - [`mvc`](#mvc)
  - [`hexagonal_modular`](#hexagonal_modular)
- [The `scaffold.yml` file](#the-scaffoldyml-file)
- [Errors](#errors)
- [Examples](#examples)

## Usage

```bash
mvn top.jagt.archboot.plugin:archboot-maven-plugin:0.1.0:init \
    -DgroupId=<groupId> \
    -DartifactId=<artifactId> \
    -Darchitecture=<architecture> \
    [other parameters]
```

`groupId`, `artifactId` and `architecture` are required. Everything else has a default.

> [!NOTE]
> The `hexagonal_modular` architecture is available from version **0.2.0**. With `0.1.0` only `mvc` is accepted.

## Parameters

Parameters are passed with `-D<property>=<value>`.

### Main values

| Parameter      | Property       | Type   | Required | Default            | Description                                                                          |
|----------------|----------------|--------|----------|--------------------|--------------------------------------------------------------------------------------|
| `groupId`      | `groupId`      | String | **Yes**  |                    | Maven `groupId` of the generated project.                                            |
| `artifactId`   | `artifactId`   | String | **Yes**  |                    | Maven `artifactId` of the generated project. Normalized to kebab-case.               |
| `architecture` | `architecture` | String | **Yes**  |                    | Architecture to generate. Case-insensitive. See the [catalog](#architecture-values). |
| `version`      | `version`      | String | No       | `0.1.0-SNAPSHOT`   | Version of the generated project.                                                    |
| `description`  | `description`  | String | No       |                    | Short description of the generated project.                                          |

### Naming

| Parameter     | Property      | Type   | Required | Default                                               | Description                       |
|---------------|---------------|--------|----------|-------------------------------------------------------|-----------------------------------|
| `name`        | `name`        | String | No       | the `artifactId` as given                             | Human-readable project name.      |
| `packageName` | `packageName` | String | No       | `groupId` + `.` + normalized `artifactId`, normalized | Base Java package.                |

### Configuration

| Parameter           | Property        | Type    | Required | Default | Description                                                                                  |
|---------------------|-----------------|---------|----------|---------|----------------------------------------------------------------------------------------------|
| `javaVersion`       | `javaVersion`   | String  | No       | `17`    | Java version used by the generated project.                                                  |
| `springVersion`     | `springVersion` | String  | No       | `4.0.0` | Spring Boot version used by the generated project.                                           |
| `configurationType` | `configuration` | String  | No       | `yml`   | Format of the application configuration file. See the [catalog](#configuration-file-values). |
| `gitKeep`           | `gitKeep`       | boolean | No       | `false` | Creates `.gitkeep` files in empty directories.                                               |

> [!NOTE]
> `configurationType` is set with the property **`configuration`**: `-Dconfiguration=properties`.

### Annotation processors

| Parameter   | Property    | Type    | Required | Default | Description                                 |
|-------------|-------------|---------|----------|---------|---------------------------------------------|
| `lombok`    | `lombok`    | boolean | No       | `false` | Adds Lombok to the generated project.       |
| `mapstruct` | `mapstruct` | boolean | No       | `false` | Adds MapStruct to the generated project.    |

When both are enabled, the generated `pom.xml` also adds `lombok-mapstruct-binding` so the two processors work together.

### Output

| Parameter   | Property | Type | Required | Default                    | Description                                                     |
|-------------|----------|------|----------|----------------------------|-----------------------------------------------------------------|
| `outputDir` | `output` | File | No       | current working directory  | Directory where the project folder (`<artifactId>`) is created. |

> [!NOTE]
> `outputDir` is set with the property **`output`**: `-Doutput=/path/to/workspace`.

## Catalog

### Architecture values

| Value (case-insensitive) | Architecture               | Status      | Details                                   |
|--------------------------|----------------------------|-------------|-------------------------------------------|
| `mvc`                    | MVC layered monolith       | Available   | [`mvc`](#mvc)                             |
| `hexagonal_modular`      | Hexagonal modular monolith | Available   | [`hexagonal_modular`](#hexagonal_modular) |

### Configuration file values

| Value        | Generated file           |
|--------------|--------------------------|
| `yml`        | `application.yml`        |
| `properties` | `application.properties` |

## What the goal does

1. Validates `architecture` and `configuration`.
2. Normalizes the inputs (see [rules](#normalization-rules)).
3. Creates the project directory `<output>/<artifactId>`.
4. Validates the Java and Spring Boot versions.
5. Writes [`scaffold.yml`](#the-scaffoldyml-file).
6. Runs the processor of the selected architecture, which generates the structure, the main class, the configuration file and the base files.

## Normalization rules

| Input                           | Rule                                                                                                                                                        | Example                                                  |
|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------|
| `artifactId`                    | camelCase and separators become kebab-case, lower case, invalid characters become `-`. Falls back to `app` if empty.                                        | `My Awesome App` → `my-awesome-app`                      |
| `name`                          | If blank, uses the `artifactId` as given (before normalization).                                                                                            | `MyApp` → `MyApp`                                        |
| `packageName`                   | If blank, built from `groupId.artifactId`: lower case, `-` becomes `_`, Java keywords get a trailing `_`, segments starting with a digit get a leading `_`. | `com.example` + `my-app` → `com.example.my_app`          |
| Main class                      | PascalCase from the `artifactId` plus the `Application` suffix. Prefixed with `App` if empty or starting with a digit.                                      | `my-app` → `MyAppApplication`                            |
| `architecture`, `configuration` | Upper-cased, `-` and spaces become `_`, then matched against the enum.                                                                                      | `mvc` → `MVC`, `hexagonal-modular` → `HEXAGONAL_MODULAR` |

An explicit `packageName` is kept as given.

## Architectures

### `mvc`

Traditional layered monolith: controller → service → repository → model.

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
    │   ├── java/<package path>/
    │   │   ├── <ClassName>Application.java
    │   │   ├── controller/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   └── service/
    │   └── resources/
    │       └── application.yml            # or application.properties
    └── test/
        └── java/
```

| Package      | Purpose                                      |
|--------------|----------------------------------------------|
| `controller` | HTTP layer (REST controllers).               |
| `service`    | Business logic.                              |
| `repository` | Data access.                                 |
| `model`      | Domain entities and models.                  |

**Generated files**

| File                                 | Content                                                                                                        |
|--------------------------------------|----------------------------------------------------------------------------------------------------------------|
| `pom.xml`                            | Spring Boot parent, dependencies (below), compiler and Spring Boot plugins.                                    |
| `<ClassName>Application.java`        | `@SpringBootApplication` entry point. Loads a `.env` file (if present) into system properties before starting. |
| `application.yml` / `.properties`    | Sets `spring.application.name` to the project `name`.                                                          |
| `.gitignore`, `.gitattributes`       | Ignore rules for Maven, IDEs and `.env`; line-ending rules for `mvnw` and `*.cmd`.                             |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/…` | Maven Wrapper (Maven 3.9.10).                                                                                  |
| `README.md`                          | Title with the project `name`.                                                                                 |
| `scaffold.yml`                       | Description of the generation inputs.                                                                          |
| `.gitkeep`                           | Only when `gitKeep=true`, in every empty leaf directory.                                                       |

**Generated dependencies**

| Dependency                                  | Condition        |
|---------------------------------------------|------------------|
| `spring-boot-starter-web`                   | Always           |
| `spring-boot-starter-validation`            | Always           |
| `springdoc-openapi-starter-webmvc-ui` 2.5.0 | Always           |
| `dotenv-java` 3.2.0                         | Always           |
| `spring-boot-starter-test` (test scope)     | Always           |
| `lombok` 1.18.32 (provided)                 | `lombok=true`    |
| `mapstruct` 1.5.5.Final                     | `mapstruct=true` |

### `hexagonal_modular`

Hexagonal (ports and adapters) architecture split into a multi-module Maven project. The root `pom.xml` is an aggregator (`packaging: pom`) that declares three modules, each one with its own `pom.xml`:

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
├── pom.xml                                # parent / aggregator
├── scaffold.yml
├── domain/
│   ├── pom.xml
│   └── src/main/java/<package path>/domain/
│       ├── model/
│       ├── gateway/
│       └── exception/
├── application/
│   ├── pom.xml
│   └── src/
│       ├── main/java/<package path>/application/
│       │   ├── usecase/
│       │   └── handler/
│       └── test/java/<package path>/application/
└── infrastructure/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/<package path>/
        │   │   ├── <ClassName>Application.java
        │   │   └── infrastructure/
        │   │       ├── config/
        │   │       ├── input/
        │   │       └── output/
        │   └── resources/
        │       └── application.yml        # or application.properties
        └── test/java/<package path>/infrastructure/
```

| Module           | Artifact                       | Depends on                      | Packages                          | Purpose                                                          |
|------------------|--------------------------------|---------------------------------|-----------------------------------|------------------------------------------------------------------|
| `domain`         | `<artifactId>-domain`          | none                            | `model`, `gateway`, `exception`   | Domain entities, ports (gateways) and domain exceptions.         |
| `application`    | `<artifactId>-application`     | `domain`                        | `usecase`, `handler`              | Use cases and handlers that orchestrate the domain.              |
| `infrastructure` | `<artifactId>-infrastructure`  | `application`, `domain`         | `config`, `input`, `output`       | Adapters (REST, persistence, etc.), configuration and bootstrap. |

The dependency rule points inwards: `infrastructure` → `application` → `domain`. The `domain` module has no dependency on Spring.

> [!NOTE]
> The main class (`<ClassName>Application.java`) and the configuration file live in the `infrastructure` module, which is the only one that repackages the executable JAR (`spring-boot-maven-plugin` is skipped in the parent and enabled with `repackage` in `infrastructure`). The `domain` module has no `src/test` directory.

**Generated files**

| File                                 | Content                                                                                                                                         |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `pom.xml` (root)                     | Spring Boot parent, `<modules>`, `dependencyManagement` (modules, springdoc, dotenv, Lombok, MapStruct) and compiler / clean plugin management. |
| `domain/pom.xml`                     | Module `<artifactId>-domain`. Only Lombok when `lombok=true`.                                                                                   |
| `application/pom.xml`                | Module `<artifactId>-application`. Depends on `domain`; see dependencies below.                                                                 |
| `infrastructure/pom.xml`             | Module `<artifactId>-infrastructure`. Depends on `application` and `domain`; includes the Spring Boot `repackage` execution.                    |
| `<ClassName>Application.java`        | `@SpringBootApplication` entry point in `infrastructure`. Loads a `.env` file (if present) into system properties before starting.              |
| `application.yml` / `.properties`    | In `infrastructure/src/main/resources`. Sets `spring.application.name` to the project `name`.                                                   |
| `.gitignore`, `.gitattributes`       | Same as `mvc`, generated in the project root.                                                                                                   |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/…` | Maven Wrapper (Maven 3.9.10), in the project root.                                                                                              |
| `README.md`                          | Title with the project `name`.                                                                                                                  |
| `scaffold.yml`                       | Description of the generation inputs (`architecture: HEXAGONAL_MODULAR`).                                                                       |
| `.gitkeep`                           | Only when `gitKeep=true`, in every empty leaf directory.                                                                                        |

**Generated dependencies per module**

| Dependency                                  | `domain`      | `application`    | `infrastructure` |
|---------------------------------------------|---------------|------------------|------------------|
| `<artifactId>-domain`                       | n/a           | Always           | Always           |
| `<artifactId>-application`                  |               | n/a              | Always           |
| `spring-context` (provided)                 |               | Always           |                  |
| `spring-boot-starter-web`                   |               |                  | Always           |
| `spring-boot-starter-validation`            |               | Always           | Always           |
| `springdoc-openapi-starter-webmvc-ui`       |               |                  | Always           |
| `dotenv-java`                               |               |                  | Always           |
| `spring-boot-starter-test` (test scope)     |               |                  | Always           |
| `lombok`                                    | `lombok=true` | `lombok=true`    | `lombok=true`    |
| `mapstruct`                                 |               | `mapstruct=true` | `mapstruct=true` |

Dependency versions (`springdoc` 2.5.0, `dotenv-java` 3.2.0, `lombok` 1.18.32, `mapstruct` 1.5.5.Final) are declared once in the root `pom.xml` and inherited by the modules.

## The `scaffold.yml` file

Written to the project root before the architecture processor runs. Example for `-Dlombok=true -Dmapstruct=true`:

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

With `-Darchitecture=hexagonal_modular` the only difference is `architecture: HEXAGONAL_MODULAR`.

## Errors

| Message                                      | Cause                                                           |
|----------------------------------------------|-----------------------------------------------------------------|
| `Invalid architecture type: <value>`         | `architecture` is not in the [catalog](#architecture-values).   |
| `Invalid configuration type: <value>`        | `configuration` is not `yml` or `properties`.                   |
| `Output directory already exists: <path>`    | `<output>/<artifactId>` already exists. Nothing is overwritten. |
| `Failed to create output directory: <path>`  | The directory could not be created (permissions, invalid path). |
| `Java version not supported: <value>`        | `javaVersion` is lower than 17.                                 |
| `Invalid Java version: <value>`              | `javaVersion` is not an integer (for example `1.8`).            |
| `Spring Boot version not supported: <value>` | The Spring Boot major version is lower than 4.                  |
| `Invalid Spring Boot version: <value>`       | `springVersion` is not in `major.minor[.patch]` numeric format. |
| `Error creating project: <detail>`           | `scaffold.yml` could not be written.                            |

## Examples

**Minimal**

```bash
mvn archboot:init -DgroupId=com.example -DartifactId=my-app -Darchitecture=mvc
```

**With `application.properties`, Java 21 and `.gitkeep` files**

```bash
mvn archboot:init \
    -DgroupId=com.example -DartifactId=orders-api -Darchitecture=mvc \
    -Dconfiguration=properties -DjavaVersion=21 -DgitKeep=true
```

**Custom name, package and output directory**

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