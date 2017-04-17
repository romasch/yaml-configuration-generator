import org.gradle.api.Plugin
import org.gradle.api.Project

class YamlConfigurationGeneratorPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply (plugin: 'ch.romasch.gradle.generated-code')
        project.generate.dependsOn project.tasks.withType(YamlConfigurationGeneratorTask.class)
    }
}