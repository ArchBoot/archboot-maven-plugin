package com.jagt.archboot.plugin.utils;

public class ConstantsPlugin {
    private ConstantsPlugin() {}

    public final static String ARTIFACT_ID = "artifactId";
    public final static String GROUP_ID = "groupId";
    public final static String VERSION = "version";
    public final static String DESCRIPTION = "description";
    public final static String VERSION_DEFAULT_SNAPSHOT = "0.1.0-SNAPSHOT";
    public final static String ARCHITECTURE = "architecture";
    public final static String NAME = "name";
    public final static String PACKAGE_NAME = "packageName";
    public final static String JAVA_VERSION = "javaVersion";
    public final static String JAVA_VERSION_17 = "17";
    public final static String SPRING_VERSION = "springVersion";
    public final static String SPRING_VERSION_V4_0_0 = "4.0.0";
    public final static String CONFIGURATION = "configuration";
    public final static String GIT_KEEP = "gitKeep";
    public final static String YAML = "yaml";
    public final static String MAPSTRUCT = "mapstruct";
    public final static String LOMBOK = "lombok";
    public final static String FALSE = "false";
    public final static String OUTPUT = "output";
    public final static String USER_DIR = "user.dir";

    public final static String SCAFFOLD_FILE = "scaffold.yml";
    public static final String SCAFFOLD = "scaffold";
    public static final String DATA = "data";
    public final static String PACKAGE = "package";
    public final static String ANNOTATIONS = "annotations";
    public final static String CONFIG = "config";
    public final static String EXTENSION = "extension";

    public static final String IMPORTS = "imports";
    public static final String POM_XML = "pom.xml";
    public static final String APPLICATION_YML = "application.yml";
    public static final String APPLICATION_PROPERTIES = "application.properties";
    public static final String CLASS_NAME = "className";
    public static final String DOT_GITIGNORE = ".gitignore";
    public static final String DOT_GITATTRIBUTES = ".gitattributes";
    public final static String DOT_GITKEEP = ".gitkeep";
    public static final String MVNW = "mvnw";
    public static final String WRAPPER = "wrapper";
    public static final String DOT_MVN = ".mvn";
    public static final String MVNW_CMD = "mvnw.cmd";
    public static final String MVNW_PROPERTIES = "maven-wrapper.properties";
    public static final String DOT_JAVA = ".java";
    public static final String DOT_FTL = ".ftl";
    public static final String DOT_RAW = ".raw";
    public static final String README_MD = "README.md";
    public final static String TEMPLATE_FTL_PATH = "templates/ftl";
    public final static String TEMPLATE_RAW_PATH = "templates/raw";
    public final static String SHARED = "shared";
    public final static String POM_SHARED = SHARED + "/" + POM_XML + DOT_FTL;
    public final static String APP_JAVA_SHARED = SHARED + "/App.java" + DOT_FTL;
    public final static String README_SHARED = SHARED + "/" + README_MD + DOT_FTL;
    public final static String GITIGNORE_RAW_SHARED = SHARED + "/" + DOT_GITIGNORE + DOT_RAW;
    public final static String GITATTRIBUTES_RAW_SHARED = SHARED + "/" + DOT_GITATTRIBUTES + DOT_RAW;
    public final static String MVNW_RAW_SHARED = SHARED + "/" + MVNW + DOT_RAW;
    public final static String MVNW_CMD_RAW_SHARED = SHARED + "/" + MVNW_CMD + DOT_RAW;
    public final static String MVNW_PROPERTIES_RAW_SHARED = SHARED + "/" + MVNW_PROPERTIES + DOT_RAW;
    public final static String MVNW_PROPERTIES_FIELD = DOT_MVN + "/" + WRAPPER + "/" + MVNW_PROPERTIES;


    public final static String SRC = "src";
    public final static String MAIN = "main";
    public final static String JAVA = "java";
    public final static String RESOURCES = "resources";
    public final static String TEST = "test";
    public final static String SRC_MAIN_JAVA = SRC + "/" + MAIN + "/" + JAVA + "/";
    public final static String SRC_MAIN_RESOURCES = SRC + "/" + MAIN + "/" + RESOURCES + "/";
    public final static String SRC_TEST_JAVA = SRC + "/" + TEST + "/" + JAVA + "/";

    public static final String MODEL = "model";
    public static final String SERVICE = "service";
    public static final String CONTROLLER = "controller";
    public static final String REPOSITORY = "repository";

}
