package ch.romasch.gradle.yaml.config.analysis

import org.yaml.snakeyaml.nodes.*

abstract class YamlVisitor<R> {

    fun visit(node: Node): R = when (node) {
        is SequenceNode -> visitSequenceNode(node)
        is MappingNode -> visitMappingNode(node)
        is ScalarNode -> visitScalarNode(node)
        is AnchorNode -> visitAnchorNode(node)
        else -> throw IllegalStateException("Unknown node currentType")
    }

    protected abstract fun visitSequenceNode(node: SequenceNode): R
    protected abstract fun visitScalarNode(node: ScalarNode): R
    protected abstract fun visitMappingNode(node: MappingNode): R
    protected abstract fun  visitAnchorNode(node: Node): R
}

abstract class YamlCheckVisitor : YamlVisitor<Unit>(){

    override fun visitSequenceNode(node: SequenceNode) = node.value.forEach(this::visit )

    override fun visitScalarNode(node: ScalarNode) = Unit

    override fun visitMappingNode(node: MappingNode) = node.value.forEach(this::visitNodeTuple)

    override fun visitAnchorNode(node: Node) = Unit

    open protected fun visitNodeTuple(tuple: NodeTuple) {
        visit(tuple.keyNode)
        visit(tuple.valueNode)
    }

}