open class SingleValueOptionParser<T>(private val valueParser: (String) -> T) :
    OptionParser<T> {
    override fun parse(args: Array<out String>, option: Option): T {
        val index = args.indexOf("-${option.value}")
        val value = args[index + 1]
        return parseValue(value)
    }

    protected open fun parseValue(value: String): T = valueParser(value)

}

