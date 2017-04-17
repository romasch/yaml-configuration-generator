package ch.romasch.gradle.yaml.config.task

import ch.romasch.gradle.yaml.config.nodes.ClassDataGenerator
import ch.romasch.gradle.yaml.config.analysis.ScalarKeyChecker
import ch.romasch.gradle.yaml.config.analysis.YamlListChecker
import ch.romasch.gradle.yaml.config.analysis.YamlNodeConverter
import org.yaml.snakeyaml.Yaml
import java.io.StringReader

fun generateJavaCode(input: Collection<String>, targetPackage: String): Map<String, String> {
        val yaml = Yaml()
        val node = yaml.compose(StringReader(input.first()))
        ScalarKeyChecker().visit(node)
        YamlListChecker().visit(node)
        val test2 = YamlNodeConverter().visit(node)

        val gen = ClassDataGenerator()
        gen.visit(test2)
        return gen.groups.values.associateBy({it.name}, {it.generate(targetPackage)})
}