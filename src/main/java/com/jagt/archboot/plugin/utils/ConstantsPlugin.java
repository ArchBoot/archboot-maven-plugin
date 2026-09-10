package com.jagt.archboot.plugin.utils;

public class ConstantsPlugin {
    private ConstantsPlugin() {}

    public static final String CLASSPATH_SEPARATOR = "/";


    public static final String ARTIFACT_ID = "artifactId";
    public static final String GROUP_ID = "groupId";
    public static final String VERSION = "version";
    public static final String DESCRIPTION = "description";
    public static final String VERSION_DEFAULT_SNAPSHOT = "0.1.0-SNAPSHOT";
    public static final String ARCHITECTURE = "architecture";
    public static final String NAME = "name";
    public static final String PACKAGE_NAME = "packageName";
    public static final String JAVA_VERSION = "javaVersion";
    public static final String JAVA_VERSION_17 = "17";
    public static final String SPRING_VERSION = "springVersion";
    public static final String SPRING_VERSION_V4_0_0 = "4.0.0";
    public static final String CONFIGURATION = "configuration";
    public static final String GIT_KEEP = "gitKeep";
    public static final String YAML = "yaml";
    public static final String MAPSTRUCT = "mapstruct";
    public static final String LOMBOK = "lombok";
    public static final String FALSE = "false";
    public static final String OUTPUT = "output";
    public static final String USER_DIR = "user.dir";

    public static final String SCAFFOLD_FILE = "scaffold.yml";
    public static final String SCAFFOLD = "scaffold";
    public static final String DATA = "data";
    public static final String PACKAGE = "package";
    public static final String ANNOTATIONS = "annotations";
    public static final String CONFIG = "config";
    public static final String EXTENSION = "extension";

    public static final String IMPORTS = "imports";
    public static final String POM_XML = "pom.xml";
    public static final String APPLICATION_YML = "application.yml";
    public static final String APPLICATION_PROPERTIES = "application.properties";
    public static final String CLASS_NAME = "className";
    public static final String DOT_GITIGNORE = ".gitignore";
    public static final String DOT_GITATTRIBUTES = ".gitattributes";
    public static final String DOT_GITKEEP = ".gitkeep";
    public static final String MVNW = "mvnw";
    public static final String WRAPPER = "wrapper";
    public static final String DOT_MVN = ".mvn";
    public static final String MVNW_CMD = "mvnw.cmd";
    public static final String MVNW_PROPERTIES = "maven-wrapper.properties";
    public static final String DOT_JAVA = ".java";
    public static final String DOT_FTL = ".ftl";
    public static final String DOT_RAW = ".raw";
    public static final String README_MD = "README.md";
    public static final String TEMPLATE_FTL_PATH = "templates/ftl";
    public static final String TEMPLATE_RAW_PATH = "templates/raw";
    public static final String SHARED = "shared";
    public static final String POM_SHARED = SHARED + CLASSPATH_SEPARATOR + POM_XML + DOT_FTL;
    public static final String APP_JAVA_SHARED = SHARED + CLASSPATH_SEPARATOR + "App.java" + DOT_FTL;
    public static final String README_SHARED = SHARED + CLASSPATH_SEPARATOR + README_MD + DOT_FTL;
    public static final String GITIGNORE_RAW_SHARED = SHARED + CLASSPATH_SEPARATOR + DOT_GITIGNORE + DOT_RAW;
    public static final String GITATTRIBUTES_RAW_SHARED = SHARED + CLASSPATH_SEPARATOR + DOT_GITATTRIBUTES + DOT_RAW;
    public static final String MVNW_RAW_SHARED = SHARED + CLASSPATH_SEPARATOR + MVNW + DOT_RAW;
    public static final String MVNW_CMD_RAW_SHARED = SHARED + CLASSPATH_SEPARATOR + MVNW_CMD + DOT_RAW;
    public static final String MVNW_PROPERTIES_RAW_SHARED = SHARED + CLASSPATH_SEPARATOR + MVNW_PROPERTIES + DOT_RAW;
    public static final String MVNW_PROPERTIES_FIELD = DOT_MVN + CLASSPATH_SEPARATOR + WRAPPER + CLASSPATH_SEPARATOR + MVNW_PROPERTIES;


    public static final String SRC = "src";
    public static final String MAIN = "main";
    public static final String JAVA = "java";
    public static final String RESOURCES = "resources";
    public static final String TEST = "test";
    public static final String SRC_MAIN_JAVA = SRC + CLASSPATH_SEPARATOR + MAIN + CLASSPATH_SEPARATOR + JAVA + CLASSPATH_SEPARATOR;
    public static final String SRC_MAIN_RESOURCES = SRC + CLASSPATH_SEPARATOR + MAIN + CLASSPATH_SEPARATOR + RESOURCES + CLASSPATH_SEPARATOR;
    public static final String SRC_TEST_JAVA = SRC + CLASSPATH_SEPARATOR + TEST + CLASSPATH_SEPARATOR + JAVA + CLASSPATH_SEPARATOR;

    public static final String MODEL = "model";
    public static final String SERVICE = "service";
    public static final String CONTROLLER = "controller";
    public static final String REPOSITORY = "repository";

}
