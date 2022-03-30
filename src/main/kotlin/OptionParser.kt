fun interface OptionParser<T> {
    fun parse(args: Array<String>, option: Option): T
}