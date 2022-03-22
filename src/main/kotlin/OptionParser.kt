interface OptionParser<T> {
    fun parse(args: Array<out String>, option: Option): T
}