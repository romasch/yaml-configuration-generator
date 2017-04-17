package ch.romasch.gradle.yaml.config.analysis

import ch.romasch.gradle.yaml.config.nodes.ConfigNode
import org.yaml.snakeyaml.nodes.*

class YamlNodeConverter : YamlVisitor<ConfigNode>() {

    override fun visitAnchorNode(node: Node): ConfigNode {
        TODO("not implemented")
    }

    override fun visitSequenceNode(node: SequenceNode): ConfigNode {
        return ConfigNode.ListConfigValue(node.value.map { ConfigNode.ScalarConfigValue(it as ScalarNode) })
    }

    override fun  visitScalarNode(node: ScalarNode) = ConfigNode.ScalarConfigValue(node)

    override fun  visitMappingNode(node: MappingNode) = ConfigNode.ConfigGroup(node.value.map(this::visitNodeTuple))

    private fun visitNodeTuple(tuple: NodeTuple): ConfigNode {
        val key = (tuple.keyNode as ScalarNode).value
        val value = visit(tuple.valueNode)
        return ConfigNode.Assignment(key, value)
    }

}
