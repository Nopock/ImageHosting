package me.nopox.image.config

private const val NAME_PREFIX = "--"

private val String.isArgumentName: Boolean
    get() = startsWith(NAME_PREFIX) && withIndex().all { (i, char) -> i < NAME_PREFIX.length || !char.isWhitespace() }

/**
 * Converts an array of program arguments into a map.
 *
 * Any arguments starting with two hyphens `--` is interpreted as a key. Any arguments between two keys are treated as
 * the first one's arguments. For example, these arguments...
 * ```text
 * --arg1 Hello World --arg2 Howdy Planet
 * ```
 * ...would produce the following map (formatted as a JSON object):
 * ```json
 * {"arg1": "Hello World", "arg2": "Howdy Planet"}
 * ```
 *
 * The only restriction is that the first element in the array must always be a key (starting with `--`). Otherwise, an
 * [IllegalArgumentException] is thrown.
 */
fun Array<String>.mappedArguments() = buildMap {
    val argumentArray = this@mappedArguments
    var i = 0

    while (i < argumentArray.size) {
        val name = argumentArray[i++].let {
            // Each named argument should start with two hyphens (--).
            if (!it.isArgumentName)
                throw IllegalArgumentException("Expected a new argument starting with $NAME_PREFIX, but instead found $it")

            // Remove the hyphens so that we're just left with the name itself.
            it.substring(NAME_PREFIX.length)
        }

        // Treat any arguments between this name and the next name (or the end) as the argument's value.
        val value = StringBuilder()
        while (i < argumentArray.size && !argumentArray[i].isArgumentName) {
            if (value.isNotEmpty()) value.append(' ')
            value.append(argumentArray[i++])
        }

        // Add the argument & its value to the resulting map.
        put(name.lowercase(), value.toString())
    }
}