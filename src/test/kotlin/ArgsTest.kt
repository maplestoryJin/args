import exception.IllegalOptionException
import exception.UnsupportedTypeOptionException
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ArgTest {
    // happy path
    // -g this is a list -d 1 2 -3 5
    // single option:
    // default value:
    // TODO: list: []

    @Test
    internal fun 不支持类型应该抛出异常() {
        val exception = assertThrows<UnsupportedTypeOptionException> {
            parse(OptionUnSupportedType::class, "-a", "abc123")
        }
        assertEquals("a", exception.option)
        assertEquals(Any::class, exception.type)

    }

    @Test
    internal fun 参数没有标注应该抛出异常() {
        val exception = assertThrows<IllegalOptionException> {
            parse(OptionWithoutAnnotation::class, "-p", "8080")
        }
        assertEquals("port", exception.parameter)
    }

    @Test
    internal fun `命令行参数解析（单值）`() {
        val arguments = parse(MultiOptions::class, "-l", "-p", "8080", "-d", "/usr/logs")
        assertEquals(true, arguments.logging)
        assertEquals(8080, arguments.port)
        assertEquals("/usr/logs", arguments.directory)

    }

    @Test
    internal fun `命令行参数解析（列表）`() {
        val arguments = parse(ListOptions::class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5")
        assertContentEquals(arrayOf("this", "is", "a", "list"), arguments.group)
        assertContentEquals(arrayOf(1, 2, -3, 5), arguments.numbers)

    }
}

data class OptionUnSupportedType(@Option("a") val data: Any)

data class OptionWithoutAnnotation(val port: Int)

data class MultiOptions(
    @Option("l") val logging: Boolean,
    @Option("p") val port: Int,
    @Option("d") val directory: String
)

data class ListOptions(@Option("g") val group: Array<String>, @Option("d") val numbers: Array<Int>)
