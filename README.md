<div align="center">

# Archboot Maven Plugin

**Generate Spring Boot project skeletons from the command line, organized by architecture.**


[![Maven Central](https://img.shields.io/maven-central/v/top.jagt.archboot.plugin/archboot-maven-plugin?logo=apachemaven&label=Maven%20Central)](https://central.sonatype.com/artifact/top.jagt.archboot.plugin/archboot-maven-plugin)
[![GitHub Tag](https://img.shields.io/github/v/tag/ArchBoot/archboot-maven-plugin?sort=semver&logo=git&label=Version)](https://github.com/ArchBoot/archboot-maven-plugin/tags)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![CI](https://github.com/ArchBoot/archboot-maven-plugin/actions/workflows/ci-app.yml/badge.svg?branch=main)](https://github.com/ArchBoot/archboot-maven-plugin/actions/workflows/ci-app.yml)

**English** | [Español](README_ES.md)

</div>

---
## Table of contents

- [About](#about)
- [Plugin coordinates](#plugin-coordinates)
- [Requirements](#requirements)
- [Usage](#usage)
    - [Option A: from Maven Central](#option-a-from-maven-central)
    - [Option B: clone and build from source](#option-b-clone-and-build-from-source)
- [Goals](#goals)
- [Quick example](#quick-example)
- [Contributing](#contributing)
- [License](#license)
## About

ArchBoot is a Maven plugin that creates a ready-to-run Spring Boot project from a single command. You choose the architecture and the plugin generates the `pom.xml`, the package layout, the main class, the configuration file, the Maven Wrapper and the base Git files, plus a `scaffold.yml` that describes what was generated.

It does **not** need an existing Maven project, so it can be run from any directory.

## Plugin coordinates

```xml
<groupId>top.jagt.archboot.plugin</groupId>
<artifactId>archboot-maven-plugin</artifactId>
<version>0.1.0</version>
```

| Property     | Value                                                         |
|--------------|---------------------------------------------------------------|
| Packaging    | `maven-plugin`                                                |
| Goal prefix  | `archboot`                                                    |
| License      | [Apache License 2.0](LICENSE)                                 |
| Java         | 17+                                                           |
| Maven        | 3.9+                                                          |
| SCM          | https://github.com/ArchBoot/archboot-maven-plugin             |
| Author       | [Jhon Alexander Gomez Trujillo](https://github.com/JAGT1806)  |

## Requirements

- JDK 17 or newer
- Maven 3.9 or newer (or use the bundled Maven Wrapper when working from source)
- Git (only for Option B)
## Usage

There are two ways to run the plugin.

### Option A: from Maven Central

No installation is needed. Maven downloads the plugin on first use.

```bash
mvn top.jagt.archboot.plugin:archboot-maven-plugin:0.1.0:init \
    -DgroupId=com.example \
    -DartifactId=my-app \
    -Darchitecture=mvc
```

**Short prefix (optional).** Register the plugin group in `~/.m2/settings.xml` to use `archboot:init` instead of the full coordinates:

```xml
<settings>
  <pluginGroups>
    <pluginGroup>top.jagt.archboot.plugin</pluginGroup>
  </pluginGroups>
</settings>
```

```bash
mvn archboot:init -DgroupId=com.example -DartifactId=my-app -Darchitecture=mvc
```

### Option B: clone and build from source

Use this option to try unreleased changes or to contribute.

**1. Clone the repository**

```bash
git clone https://github.com/ArchBoot/archboot-maven-plugin.git
cd archboot-maven-plugin
```

**2. Build and install the plugin into your local repository**

```bash
./mvnw clean install        # Linux / macOS
mvnw.cmd clean install      # Windows
```

**3. Run the goal** with the version you just installed (`0.1.0-SNAPSHOT`):

```bash
mvn top.jagt.archboot.plugin:archboot-maven-plugin:0.1.0-SNAPSHOT:init \
    -DgroupId=com.example \
    -DartifactId=my-app \
    -Darchitecture=mvc \
    -Doutput=/path/to/workspace
```

> [!NOTE]
> The generated `pom.xml` declares `archboot-maven-plugin` at version `0.1.0`. That version must be resolvable (from Maven Central or from your local repository) when you build the generated project.

## Goals

| Goal                                    | Description                                                           | Needs a Maven project | Documentation                                                               |
|-----------------------------------------|-----------------------------------------------------------------------|-----------------------|-----------------------------------------------------------------------------|
| [`archboot:init`](docs/goals/init.md)   | Generates a new Spring Boot project skeleton for a given architecture | No                    | [English](docs/goals/init.md) · [Español](docs/goals/init.es.md)            |

Each goal page describes its parameters, the catalog of accepted values and the project structure generated for every architecture.

## Quick example

```bash
mvn archboot:init \
    -DgroupId=com.example \
    -DartifactId=my-app \
    -Darchitecture=mvc \
    -Dlombok=true \
    -Dmapstruct=true
```

Result:

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

## Contributing

1. Fork the repository and create a branch from `develop` (`feature/*`, `bugfix/*`, `hotfix/*`).
2. Run `./mvnw clean verify` before opening a pull request.
3. Open the pull request against `develop`.
## License

Distributed under the [Apache License 2.0](LICENSE).