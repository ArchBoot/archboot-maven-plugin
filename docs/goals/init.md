# Goal `archboot:init`

[← Back to README](../../README.md) · **English** | [Español](init.es.md)

Generates a new Spring Boot project skeleton for the chosen architecture.

| Attribute          | Value                                                 |
|--------------------|-------------------------------------------------------|
| Full name          | `com.jagt.archboot.plugin:archboot-maven-plugin:init` |
| Short name         | `archboot:init`                                       |
| Since              | 0.1.0                                                 |
| Requires a project | No (`requiresProject = false`)                        |
| Implementation     | `com.jagt.archboot.plugin.mojo.InitProjectMojo`       |

## Table of contents

- [Usage](#usage)
- [Parameters](#parameters)
- [Catalog](#catalog)
- [What the goal does](#what-the-goal-does)
- [Normalization rules](#normalization-rules)
- [Architectures](#architectures)
    - [`mvc`](#mvc)
- [The `scaffold.yml` file](#the-scaffoldyml-file)
- [Errors](#errors)
- [Examples](#examples)

## Usage

```bash
mvn com.jagt.archboot.plugin:archboot-maven-plugin:0.1.0:init \
    -DgroupId=<groupId> \
    -DartifactId=<artifactId> \
    -Darchitecture=<architecture> \
    [other parameters]
```

`groupId`, `artifactId` and `architecture` are required. Everything else has a default.

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

| Value (case-insensitive) | Architecture              | Status    | Details        |
|--------------------------|---------------------------|-----------|----------------|
| `mvc`                    | MVC layered monolith      | Available | [`mvc`](#mvc)  |

### Configuration file values

| Value        | Generated file           |
|--------------|--------------------------|
| `yml`        | `application.yml`        |
| `properties` | `application.properties` |

## What the goal does

1. Validates `architecture` and `configuration`.
2. Normalizes the inputs (see [rules](#normalization-rules)).
3. Creates the project directory `<output>/<artifactId>`.
4. Writes [`scaffold.yml`](#the-scaffoldyml-file).
5. Runs the processor of the selected architecture, which generates the structure, the main class, the configuration file and the base files.

## Normalization rules

| Input                           | Rule                                                                                                                                                        | Example                                         |
|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|
| `artifactId`                    | camelCase and separators become kebab-case, lower case, invalid characters become `-`. Falls back to `app` if empty.                                        | `My Awesome App` → `my-awesome-app`             |
| `name`                          | If blank, uses the `artifactId` as given (before normalization).                                                                                            | `MyApp` → `MyApp`                               |
| `packageName`                   | If blank, built from `groupId.artifactId`: lower case, `-` becomes `_`, Java keywords get a trailing `_`, segments starting with a digit get a leading `_`. | `com.example` + `my-app` → `com.example.my_app` |
| Main class                      | PascalCase from the `artifactId` plus the `Application` suffix. Prefixed with `App` if empty or starting with a digit.                                      | `my-app` → `MyAppApplication`                   |
| `architecture`, `configuration` | Upper-cased, `-` and spaces become `_`, then matched against the enum.                                                                                      | `mvc` → `MVC`                                   |

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

| File                                   | Content                                                                                   |
|----------------------------------------|-------------------------------------------------------------------------------------------|
| `pom.xml`                              | Spring Boot parent, dependencies (below), compiler and Spring Boot plugins.               |
| `<ClassName>Application.java`          | `@SpringBootApplication` entry point. Loads a `.env` file (if present) into system properties before starting. |
| `application.yml` / `.properties`      | Sets `spring.application.name` to the project `name`.                                     |
| `.gitignore`, `.gitattributes`         | Ignore rules for Maven, IDEs and `.env`; line-ending rules for `mvnw` and `*.cmd`.        |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/…`   | Maven Wrapper (Maven 3.9.10).                                                             |
| `README.md`                            | Title with the project `name`.                                                            |
| `scaffold.yml`                         | Description of the generation inputs.                                                     |
| `.gitkeep`                             | Only when `gitKeep=true`, in every empty leaf directory.                                  |

**Generated dependencies**

| Dependency                                   | Condition       |
|----------------------------------------------|-----------------|
| `spring-boot-starter-web`                    | Always          |
| `spring-boot-starter-validation`             | Always          |
| `springdoc-openapi-starter-webmvc-ui` 2.5.0  | Always          |
| `dotenv-java` 3.2.0                          | Always          |
| `spring-boot-starter-test` (test scope)      | Always          |
| `lombok` 1.18.32 (provided)                  | `lombok=true`   |
| `mapstruct` 1.5.5.Final                      | `mapstruct=true`|

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

## Errors

| Message                                         | Cause                                                       |
|-------------------------------------------------|-------------------------------------------------------------|
| `Invalid architecture type: <value>`            | `architecture` is not in the [catalog](#architecture-values). |
| `Invalid configuration type: <value>`           | `configuration` is not `yml` or `properties`.               |
| `Output directory already exists: <path>`       | `<output>/<artifactId>` already exists. Nothing is overwritten. |
| `Failed to create output directory: <path>`     | The directory could not be created (permissions, invalid path). |
| `Error creating project: <detail>`              | `scaffold.yml` could not be written.                        |

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