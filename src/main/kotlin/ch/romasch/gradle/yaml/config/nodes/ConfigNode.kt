package ch.romasch.gradle.yaml.config.nodes

import org.yaml.snakeyaml.nodes.ScalarNode

sealed class ConfigNode {
    class Assignment(val name: String, val value: ConfigNode) : ConfigNode()
    class ConfigGroup(val members: List<ConfigNode>) : ConfigNode()
    class ScalarConfigValue(val node: ScalarNode) : ConfigNode()
    class ListConfigValue(val nodes: List<ScalarConfigValue>) : ConfigNode()
}

abstract class ConfigVisitor {

    fun visit(config: ConfigNode):Unit = when(config) {
        is ConfigNode.Assignment -> visitAssignment(config)
        is ConfigNode.ConfigGroup -> visitConfigGroup(config)
        is ConfigNode.ScalarConfigValue -> visitScalarConfigValue(config)
        is ConfigNode.ListConfigValue -> visitListConfigValue(config)
    }

    open fun visitAssignment(assignment: ConfigNode.Assignment) = visit(assignment.value)
    open fun visitConfigGroup(group: ConfigNode.ConfigGroup) = group.members.forEach(this::visit)
    open fun visitScalarConfigValue(scalar: ConfigNode.ScalarConfigValue) = Unit
    open fun visitListConfigValue( list: ConfigNode.ListConfigValue) = list.nodes.forEach(this::visit)
}