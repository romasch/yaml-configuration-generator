import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.nio.file.Path

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import static org.junit.Assert.assertTrue

class FunctionalPluginTest {
    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile


    @Before
    void setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'ch.romasch.gradle.yaml-configuration-generator'
            }
            """.stripIndent()
    }

    @Test
    void smokeTest() {
        def result = executeGradleBuild()
        assertTrue("Task outcome should be successful", result.task(":build").outcome == SUCCESS)
    }

    @Test
    void packageInfoWithDefault() {

        buildFile << """
            task myGenerate (type: YamlConfigurationGeneratorTask)
            """.stripIndent()

        appendYaml()
        def result = executeGradleBuild()

        File expected = new File(testProjectDir.root, 'src/gen/java/yaml/config/package-info.java')
        assertTrue("File ${expected} should exist", expected.exists())
        assertTrue("Package name should be default", expected.text.contains('package yaml.config;'))
        assertTrue("Task outcome should be successful", result.task(":build").outcome == SUCCESS)
    }

    @Test
    void packageInfoConfigured() {

        buildFile << """
            task myGenerate (type: YamlConfigurationGeneratorTask) {
                targetPackage 'my.test'
            }
            """.stripIndent()

        appendYaml()
        def result = executeGradleBuild()

        File expected = new File(testProjectDir.root, 'src/gen/java/my/test/package-info.java')
        assertTrue("File ${expected} should exist", expected.exists())
        assertTrue("Package name should be my.test", expected.text.contains('package my.test;'))
        assertTrue("Task outcome should be successful", result.task(":build").outcome == SUCCESS)
    }

    @Test
    void incrementalBuild() {
        buildFile << """
            task myGenerate (type: YamlConfigurationGeneratorTask)
            """.stripIndent()
        appendYaml()
        def first = executeGradleBuild()
        assertTrue("myGenerate should execute", first.task(":myGenerate").outcome == SUCCESS)

        def second = executeGradleBuild()
        assertTrue("myGenerate should be up-to-date", second.task(":myGenerate").outcome == UP_TO_DATE)

        appendYaml("appended: 41")
        def third = executeGradleBuild()
        assertTrue("myGenerate should run again after input change", third.task(":myGenerate").outcome == SUCCESS)


        File packageInfo = new File(testProjectDir.root, 'src/gen/java/yaml/config/package-info.java')
        assertTrue("package-info.java should exist", packageInfo.exists())
        packageInfo.delete()

        def fourth = executeGradleBuild()
        assertTrue("myGenerate should run again after output change", fourth.task(":myGenerate").outcome == SUCCESS)
    }



    private void appendYaml(String text = "test: 42") {
        Path folder = testProjectDir.root.toPath()
                .resolve('src')
                .resolve('main')
                .resolve('resources')
        folder.toFile().mkdirs()
        File yaml = folder.resolve('test.config.yaml').toFile()
        if (!yaml.exists()) {
            yaml.createNewFile()
        }
        yaml << "$text\n"
    }

    private BuildResult executeGradleBuild() {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(':build')
                .withPluginClasspath()
                .build()
    }


}
