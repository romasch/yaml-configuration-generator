package ch.romasch.gradle.yaml.config.analysis

import org.yaml.snakeyaml.nodes.*

class ScalarKeyChecker: YamlCheckVisitor() {

    override fun visitNodeTuple(tuple: NodeTuple) {
        checkValidKey(tuple.keyNode)
        super.visitNodeTuple(tuple)
    }

    private fun checkValidKey(node: Node) {
        if (node is ScalarNode) {
            val value = node.value
            if (!value.isNullOrEmpty()) {
                if (value == "<<") {
                    return
                }
                if (value[0].isJavaIdentifierStart() && value.all(Char::isJavaIdentifierPart)) {
                    return
                }
            }
        }
        throw IllegalStateException("Invalid node" + node)
    }

}
