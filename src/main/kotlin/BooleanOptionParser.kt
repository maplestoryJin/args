class BooleanOptionParser : OptionParser<Boolean> {
    override fun parse(args: Array<out String>, option: Option): Boolean {
        return args.contains("-${option.value}")
    }

}