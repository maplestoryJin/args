package exception

import kotlin.reflect.KClass

class UnsupportedTypeOptionException(val option: String, val type: KClass<*>) : Exception()
