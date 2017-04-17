package ch.romasch.gradle.yaml.config.generation
class ClassData(val name: String) {
    val attributes: MutableMap<String, String> = HashMap()

    fun generate(packageName: String): String {
        val sb = StringBuilder()
        sb.append("package $packageName;")
        sb.append("\n\npublic final class $name {\n")

        // members
        attributes.forEach { name, type ->
            sb.append("\n\tprivate final $type ${lower(name)};")
        }

        // Constructor
        sb.append("\n\n\tpublic $name(")
        attributes.map(this::combineEntry).joinTo(sb, separator=", ")
        sb.append(") {")
        attributes.keys.map(this::lower).forEach { name ->
            sb.append("\n\t\tthis.$name = $name;")
        }
        sb.append("\n\t}")

        // Getters
        attributes.forEach {name, type ->
            sb.append("\n\n\tpublic $type get${name}() {")
            sb.append("\n\t\treturn ${lower(name)};")
            sb.append("\n\t}")
        }


        // override()
        sb.append("\n\n\tpublic $name override(java.util.Map yaml) {")
        attributes.forEach {name, type ->
            sb.append("\n\t\t$type ${lower(name)} = this.${lower(name)};")
            sb.append("\n\t\tif (yaml.containsKey(\"$name\") && yaml.get(\"$name\") instanceof ${yamlType(type)}) {")
            sb.append("\n\t\t\t${lower(name)} = ")
            if (yamlType(type) == "Map") {
                sb.append("${lower(name)}.override((java.util.Map) yaml.get(\"$name\"));")
            } else {
                sb.append("($type) yaml.get(\"$name\");")
            }
            sb.append("\n\t\t}")
        }

        sb.append("\n\t\treturn new $name(")
        attributes.keys.map(this::lower).joinTo(sb, separator=", ")
        sb.append(");")

        sb.append("\n\t}")

        sb.append("\n}\n")
        return sb.toString()
    }

    private fun lower(name: String) = name[0].toLowerCase() + name.substring(1)

    private fun yamlType(type: String) = when (type) {
        "Integer" -> "Integer"
        "Boolean" -> "Boolean"
        "String" -> "String"
        else -> "java.util.Map"
    }

    private fun combineEntry(entry: Map.Entry<String, String>) = entry.value + " " + lower(entry.key)

}
