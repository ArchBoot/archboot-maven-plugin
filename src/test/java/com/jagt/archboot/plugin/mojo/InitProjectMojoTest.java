package com.jagt.archboot.plugin.mojo;

import com.jagt.archboot.plugin.model.ScaffoldModel;
import com.jagt.archboot.plugin.model.enums.ArchitectureType;
import com.jagt.archboot.plugin.model.enums.ConfigApplicationType;
import com.jagt.archboot.plugin.mojo.tools.TestableInitProjectMojo;
import com.jagt.archboot.plugin.utils.ConstantsPlugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InitProjectMojoTest {
    private TestableInitProjectMojo mojo;

    @BeforeEach
    void setup() {
        mojo = new TestableInitProjectMojo();
        mojo.setLog(new SystemStreamLog());

        mojo.setArchitecture("mvc");
        mojo.setConfigurationType("yml");
        mojo.setGroupId("com.example");
        mojo.setArtifactId("MyApp");
        mojo.setVersion("1.0.0-SNAPSHOT");
        mojo.setJavaVersion("17");
        mojo.setSpringVersion("4.0.0");

        mojo.setName("my-app");
        mojo.setPackageName("com.example.myapp");
        mojo.setDescription(null);

        mojo.setGitKeep(false);
        mojo.setLombok(false);
        mojo.setMapstruct(false);
    }

    @Test
    void generatesScaffoldYaml_happyPath(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);

        mojo.execute();

        File projectDir = new File(tempDir, "my-app");

        File scaffold = new File(projectDir, "scaffold.yml");

        assertThat(projectDir).exists().isDirectory();

        assertThat(scaffold).exists().isFile();

        String content = Files.readString(scaffold.toPath());

        assertThat(content)
                .contains("architecture: MVC")
                .contains("artifactId: my-app")
                .contains("groupId: com.example");
    }

    @Test
    void blankName_fallsBackToArtifactId(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);
        mojo.setName("   ");

        mojo.execute();

        File scaffold = new File(new File(tempDir, "my-app"), "scaffold.yml");

        assertThat(scaffold).exists();

        String content = Files.readString(scaffold.toPath());

        assertThat(content).contains("name: MyApp");
    }

    @Test
    void blankPackageName_isDerivedFromGroupAndArtifact(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);
        mojo.setPackageName(null);

        mojo.execute();

        File scaffold = new File(new File(tempDir, "my-app"), "scaffold.yml");

        assertThat(scaffold).exists();

        String content = Files.readString(scaffold.toPath());

        assertThat(content).contains("packageName: com.example.my_app");
    }

    @Test
    void explicitPackageName_isPreserved(@TempDir File tempDir) throws Exception {

        mojo.setOutputDir(tempDir);
        mojo.setPackageName("com.custom.application");

        mojo.execute();

        File scaffold = new File(new File(tempDir, "my-app"), "scaffold.yml");

        String content = Files.readString(scaffold.toPath());

        assertThat(content).contains("packageName: com.custom.application");
    } // I think delete this

    @Test
    void explicitName_isPreserved(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);
        mojo.setName("My Custom Application");

        mojo.execute();

        File scaffold = new File(new File(tempDir, "my-app"), "scaffold.yml");

        String content = Files.readString(scaffold.toPath());

        assertThat(content).contains("name: My Custom Application");
    }

    @Test
    void artifactId_isNormalized(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);
        mojo.setArtifactId("My Awesome App");

        mojo.execute();

        File projectDir = new File(tempDir, "my-awesome-app");

        assertThat(projectDir).exists().isDirectory();

        assertThat(new File(projectDir, "scaffold.yml")).exists();
    }

    @Test
    void invalidArchitecture_throws() {
        mojo.setArchitecture("does-not-exist");

        assertThatThrownBy(() -> mojo.execute())
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining("Invalid architecture type");
    }

    @Test
    void invalidConfigurationType_throws() {
        mojo.setConfigurationType("xml");

        assertThatThrownBy(() -> mojo.execute())
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining(
                        "Invalid configuration type"
                );
    }

    @Test
    void outputDirAlreadyExists_throws(@TempDir File tempDir) throws Exception {
        File existing = new File(tempDir, "my-app");

        Files.createDirectories(existing.toPath());

        mojo.setOutputDir(tempDir);

        assertThatThrownBy(() -> mojo.execute())
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void nullOutputDir_usesUserDir(@TempDir File tempDir) throws Exception {
        String originalUserDir = System.getProperty("user.dir");

        try {
            System.setProperty("user.dir", tempDir.getAbsolutePath());

            mojo.setOutputDir(null);

            mojo.execute();

            File projectDir = new File(tempDir, "my-app");

            assertThat(projectDir)
                    .exists()
                    .isDirectory();

            assertThat(new File(projectDir, "scaffold.yml")).exists();

        } finally {
            System.setProperty("user.dir", originalUserDir);
        }
    }

    @Test
    void annotations_areWrittenToScaffold(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);
        mojo.setLombok(true);
        mojo.setMapstruct(true);

        mojo.execute();

        File scaffold = new File(new File(tempDir, "my-app"), "scaffold.yml");

        String content = Files.readString(scaffold.toPath());

        assertThat(content)
                .contains("lombok: true")
                .contains("mapstruct: true");
    }

    @Test
    void outputDirEqualsClasspathSeparator_returnsBaseDirDirectly() throws Exception {
        mojo.setOutputDir(new File(ConstantsPlugin.CLASSPATH_SEPARATOR));

        Method validateOutputDir = InitProjectMojo.class.getDeclaredMethod("validateOutputDir");
        validateOutputDir.setAccessible(true);

        File result = (File) validateOutputDir.invoke(mojo);

        assertThat(result).isEqualTo(new File(ConstantsPlugin.CLASSPATH_SEPARATOR));
    }

    @Test
    void outputDirEqualsBackslash_returnsBaseDirDirectly() throws Exception {
        mojo.setOutputDir(new File("\\"));

        Method validateOutputDir = InitProjectMojo.class.getDeclaredMethod("validateOutputDir");
        validateOutputDir.setAccessible(true);

        File result = (File) validateOutputDir.invoke(mojo);

        assertThat(result).isEqualTo(new File("\\"));
    }

    @Test
    void outputDirCreationFails_wrapsIOException(@TempDir File tempDir) throws Exception {
        // "blocker" es un archivo regular, no un directorio:
        // Files.createDirectories fallará al intentar crear "blocker/my-app"
        File blocker = new File(tempDir, "blocker");
        Files.createFile(blocker.toPath());

        mojo.setOutputDir(blocker);

        assertThatThrownBy(() -> mojo.execute())
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining("Failed to create output directory");
    }

    @Test
    void yamlWriteFails_wrapsIOException(@TempDir File tempDir) throws Exception {
        File fakeProjectDir = new File(tempDir, "not-a-directory");
        Files.createFile(fakeProjectDir.toPath());

        Method generateScaffoldModel = InitProjectMojo.class.getDeclaredMethod(
                "generateScaffoldModel", ArchitectureType.class, ConfigApplicationType.class);
        generateScaffoldModel.setAccessible(true);

        Object scaffoldModel = generateScaffoldModel.invoke(
                mojo, ArchitectureType.MVC, ConfigApplicationType.YML);

        Method generateScaffoldYaml = InitProjectMojo.class.getDeclaredMethod(
                "generateScaffoldYaml", ScaffoldModel.class, File.class);
        generateScaffoldYaml.setAccessible(true);

        assertThatThrownBy(() -> {
            try {
                generateScaffoldYaml.invoke(mojo, scaffoldModel, fakeProjectDir);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        })
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining("Error creating project");
    }

    @Test
    void generatesScaffoldYaml_withGitKeep_happyPath(@TempDir File tempDir) throws Exception {
        mojo.setOutputDir(tempDir);
        mojo.setGitKeep(true);

        mojo.execute();

        File projectDir = new File(tempDir, "my-app");

        File scaffold = new File(projectDir, "scaffold.yml");

        assertThat(projectDir).exists().isDirectory();
        assertThat(scaffold).exists().isFile();

        String content = Files.readString(scaffold.toPath());
        assertThat(content)
                .contains("architecture: MVC")
                .contains("artifactId: my-app")
                .contains("groupId: com.example");
    }

}