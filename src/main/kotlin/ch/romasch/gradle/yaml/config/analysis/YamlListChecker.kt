package ch.romasch.gradle.yaml.config.analysis

import org.yaml.snakeyaml.nodes.ScalarNode
import org.yaml.snakeyaml.nodes.SequenceNode

class YamlListChecker : YamlCheckVisitor(){

    override fun visitSequenceNode(node: SequenceNode) {
        check (node.value.map({it as ScalarNode }).map {it.tag}.distinct().count() <= 1)
        super.visitSequenceNode(node)
    }
}
