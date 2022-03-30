import exception.IllegalValueException
import exception.InsufficientArgumentsException
import exception.TooManyArgumentsException

private fun values(args: Array<String>, option: Option, expectedSize: Int): Array<String>? {
    val index = args.indexOf("-${option.value}")
    return if (index == -1) {
        null
    } else {
        checkSize(values(index, args), option, expectedSize)
    }
}

private fun checkSize(values: Array<String>, option: Option, expectedSize: Int): Array<String> {
    if (values.size < expectedSize) throw InsufficientArgumentsException(option.value)
    if (values.size > expectedSize) throw TooManyArgumentsException(option.value)
    return values
}


inline fun values(index: Int, args: Array<String>): Array<String> {
    return args.copyOfRange(index + 1,
        (index + 1..args.lastIndex).firstOrNull { args[it].matches(Regex("-[a-zA-Z]+")) } ?: args.size)
}


fun <T> parseValue(option: Option, value: String, valueParser: (String) -> T): T {
    return try {
        valueParser(value)
    } catch (e: Exception) {
        throw IllegalValueException(option.value, value)
    }
}


fun bool(): OptionParser<Boolean> = OptionParser { args, option -> values(args, option, 0) != null }

fun <T> unary(defaultValue: T, valueParser: (String) -> T) = OptionParser { args, option ->
    parseValue(
        option, values(args, option, 1)?.first() ?: return@OptionParser defaultValue, valueParser
    )
}

inline fun <reified T : Any> list(defaultValue: Array<T>, noinline valueParser: (String) -> T) =
    OptionParser { args, option ->
        val index = args.indexOf("-${option.value}")
        return@OptionParser if (index == -1) {
            defaultValue
        } else {
            values(index, args).map { parseValue(option, it, valueParser) }.toTypedArray()
        }
    }