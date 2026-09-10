<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>${springVersion}</version>
        <relativePath/>
    </parent>

    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
    <name>${name}</name>

    <properties>
        <java.version>${javaVersion}</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <#if mapstruct>
            <mapstruct.version>1.5.5.Final</mapstruct.version>
        </#if>
        <#if lombok>
            <lombok.version>1.18.32</lombok.version>
        </#if>
        <#if lombok && mapstruct>
            <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
        </#if>
        <springdoc.version>2.5.0</springdoc.version>
        <java-dotenv.version>3.2.0</java-dotenv.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${r"${springdoc.version}"}</version>
        </dependency>
        <dependency>
            <groupId>io.github.cdimascio</groupId>
            <artifactId>dotenv-java</artifactId>
            <version>${r"${java-dotenv.version}"}</version>
        </dependency>
        <#if lombok>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${r"${lombok.version}"}</version>
                <scope>provided</scope>
            </dependency>
        </#if>
        <#if mapstruct>
            <dependency>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct</artifactId>
                <version>${r"${mapstruct.version}"}</version>
            </dependency>
        </#if>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <#if lombok>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </#if>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${r"${java.version}"}</source>
                    <target>${r"${java.version}"}</target>
                    <encoding>UTF-8</encoding>
                    <#if lombok || mapstruct>
                        <annotationProcessorPaths>
                            <#if lombok>
                                <path>
                                    <groupId>org.projectlombok</groupId>
                                    <artifactId>lombok</artifactId>
                                    <version>${r"${lombok.version}"}</version>
                                </path>
                            </#if>
                            <#if mapstruct>
                                <path>
                                    <groupId>org.mapstruct</groupId>
                                    <artifactId>mapstruct-processor</artifactId>
                                    <version>${r"${mapstruct.version}"}</version>
                                </path>
                            </#if>
                            <#if lombok && mapstruct>
                                <path>
                                    <groupId>org.projectlombok</groupId>
                                    <artifactId>lombok-mapstruct-binding</artifactId>
                                    <version>${r"${lombok-mapstruct-binding.version}"}</version>
                                </path>
                            </#if>
                        </annotationProcessorPaths>
                    </#if>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-clean-plugin</artifactId>
                <configuration>
                    <failOnError>false</failOnError>
                </configuration>
            </plugin>
            <plugin>
                <groupId>com.jagt.archboot.plugin</groupId>
                <artifactId>archboot-maven-plugin</artifactId>
                <version>0.1.0</version>
            </plugin>
        </plugins>
    </build>
</project>
