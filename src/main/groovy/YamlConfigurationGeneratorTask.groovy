import ch.romasch.gradle.yaml.config.task.YamlConfigGeneratorTaskImpl
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class YamlConfigurationGeneratorTask extends DefaultTask {

    @Input
    String targetPackage = "yaml.config"

    @Input
    String targetDirectory = "src/gen/java"

    @Input
    String sourceDirectory = "src/main/resources"

    @Input
    String filePattern =  "**/*.config.yaml"

    @InputFiles
    Closure<FileTree> source = {
        def result =  project.fileTree(sourceDirectory)
        result.include(filePattern)
        return result
    }

    @OutputDirectory
    File getTarget() {
        return project.file(targetDirectory + "/" + targetPackage.replace('.', '/'))
    }

    @TaskAction
    def generate() {
        getTarget().eachFileRecurse {it.delete()}
        def mainTask = new YamlConfigGeneratorTaskImpl(source.call().files.collect({it.text}), targetPackage, getTarget())
        mainTask.execute()
    }
}
