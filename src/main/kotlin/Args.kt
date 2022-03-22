import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

fun <T : Any> parse(option: KClass<T>, vararg args: String): T {
    val constructor = option.constructors.first()
    val value = constructor.parameters.map { parseOption(it, args) }.toTypedArray()
    return constructor.call(*value)
}

private fun parseOption(parameter: KParameter, args: Array<out String>): Any? {
    return PARSERS[parameter.type.jvmErasure]?.parse(args, parameter.findAnnotation()!!)
}

private val PARSERS = mapOf(
    Boolean::class to BooleanOptionParser(),
    Int::class to SingleValueOptionParser(String::toInt),
    String::class to SingleValueOptionParser(String::toString)
)

