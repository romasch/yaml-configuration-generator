package ch.romasch.gradle.yaml.config.generation

class JavaCodeWriter {

    private val buffer = StringBuffer()
    private var indentation = 0

    fun beginBlock() : JavaCodeWriter {
        ++indentation
        buffer.append(" {")
        return this
    }
    fun endBlock(): JavaCodeWriter {
        --indentation
        writeln("}")
        return this
    }

    fun writeln(): JavaCodeWriter {
        buffer.appendln()
        return this
    }

    fun writeln(s: String): JavaCodeWriter {
        buffer
                .appendln()
                .append("\t".repeat(indentation))
                .append(s)
        return this
    }

    fun writelnEach(strings: List<String>): JavaCodeWriter {
        strings.map(this::writeln)
        return this
    }

    fun write(s: String): JavaCodeWriter {
        buffer.append(s)
        return this
    }

    fun writeArgumentList(strings: List<String>): JavaCodeWriter {
        strings.joinTo(buffer, separator = ", ", prefix = "(", postfix = ")")
        return this
    }

    override fun toString() = buffer.toString()

}