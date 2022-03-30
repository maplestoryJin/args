import exception.IllegalOptionException
import exception.UnsupportedTypeOptionException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

fun <T : Any> parse(option: KClass<T>, vararg args: String): T {
    val constructor = option.constructors.first()
    val value = constructor.parameters.map { parseOption(it, args as Array<String>) }.toTypedArray()
    return constructor.call(*value)
}

private fun parseOption(parameter: KParameter, args: Array<String>): Any {
    if (!parameter.hasAnnotation<Option>()) throw IllegalOptionException(parameter.name!!)
    val option: Option = parameter.findAnnotation()!!
    if (!PARSERS.containsKey(parameter.type.jvmErasure)) {
        throw UnsupportedTypeOptionException(option.value, parameter.type.jvmErasure)
    }
    return PARSERS[parameter.type.jvmErasure]!!.parse(args, option)
}

private val PARSERS = mapOf(
    Boolean::class to bool(),
    Int::class to unary(0, String::toInt),
    IntArray::class to list(arrayOf(), String::toInt),
    Array<String>::class to list(arrayOf(), String::toString),
    String::class to unary("", String::toString)
)

