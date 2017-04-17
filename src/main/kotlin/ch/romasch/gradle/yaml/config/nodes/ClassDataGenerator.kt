package ch.romasch.gradle.yaml.config.nodes

import ch.romasch.gradle.yaml.config.generation.ClassData
import org.yaml.snakeyaml.nodes.Tag
import java.util.*
import kotlin.collections.HashMap


class ClassDataGenerator : ConfigVisitor() {

    val groups: MutableMap<String, ClassData> = HashMap()
    val classes: Stack<ClassData> = Stack()

    var currentType: String = "UNKNOWN"
    var currentClass = "RootConfigType"

    private fun getGroup(name: String): ClassData {
        groups.putIfAbsent(name, ClassData(name))
        return groups[name]!!
    }

    override fun visitAssignment(assignment: ConfigNode.Assignment) {
        currentClass = assignment.name + "Type"
        super.visitAssignment(assignment)
        classes.peek().attributes.put(assignment.name, currentType)
    }


    override fun visitConfigGroup(group: ConfigNode.ConfigGroup) {
        classes.push(getGroup(currentClass))
        super.visitConfigGroup(group)
        val myClass= classes.pop()
        currentType = myClass.name
    }

    override fun visitScalarConfigValue(scalar: ConfigNode.ScalarConfigValue) {
        when (scalar.node.tag) {
            Tag.INT -> currentType = "Integer"
            Tag.BOOL -> currentType = "Boolean"
            Tag.STR -> currentType = "String"
            else -> currentType = "UNKNOWN"
        }
    }

    override fun visitListConfigValue( list: ConfigNode.ListConfigValue) {
        super.visitListConfigValue(list)
        currentType = "java.util.List<$currentType>"
    }

}
