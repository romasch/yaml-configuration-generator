package ch.romasch.gradle.yaml.config.generation
class ClassData(val name: String) {
    val attributes: MutableMap<String, String> = HashMap()

    fun generate(packageName: String): String {

        val writer = JavaCodeWriter()
        writer
                .write("package $packageName;")
                .writeln()
                .writeln("public final class $name")
                .beginBlock()
                .writeln()

        // members
        attributes.forEach { name, type ->
            writer.writeln("private final $type ${lower(name)};")
        }

        // Constructor
        writer.writeln()
                .writeln("public $name")
                .writeArgumentList(attributes.map(this::combineEntry))
                .beginBlock()
                .writelnEach(attributes.keys.map(this::lower).map(this::generateAssignmentPart))
                .endBlock()

        // Getters
        attributes.forEach {name, type ->
            writer.writeln()
                    .writeln("public $type get${name}()")
                    .beginBlock()
                    .writeln("return ${lower(name)};")
                    .endBlock()
        }


        // override()
        writer.writeln()
                .writeln("public $name override(java.util.Map yaml)")
                .beginBlock()

        attributes.forEach {name, type ->
            writer.writeln("$type ${lower(name)} = this.${lower(name)};")
                    .writeln("if (yaml.containsKey(\"$name\") && yaml.get(\"$name\") instanceof ${yamlType(type)})")
                    .beginBlock()
                    .writeln("${lower(name)} = ")
                    .write(generateOverridePart(name, type))
                    .endBlock()
        }

        writer.writeln("return new $name")
                .writeArgumentList(attributes.keys.map(this::lower))
                .write(";")
                .endBlock()


        // end class block
        return writer
                .endBlock()
                .writeln()
                .toString()
    }

    private fun generateAssignmentPart(name: String) = "this.$name = $name;"

    private fun generateOverridePart(name: String, type: String): String =
            if (yamlType(type) == "Map") "${lower(name)}.override((java.util.Map) yaml.get(\"$name\"));"
            else "($type) yaml.get(\"$name\");"

    private fun lower(name: String) = name[0].toLowerCase() + name.substring(1)

    private fun yamlType(type: String) = when (type) {
        "Integer" -> "Integer"
        "Boolean" -> "Boolean"
        "String" -> "String"
        else -> "java.util.Map"
    }

    private fun combineEntry(entry: Map.Entry<String, String>) = entry.value + " " + lower(entry.key)

}
