import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ArgTest {
    // happy path
    // -g this is a list -d 1 2 -3 5
    // single option:
    // sad path:
    // TODO: Boolean -l t / -l t f
    // TODO:int -p / -p 8080 8081
    // TODO:string -d / -d /usr/logs /usr/vars
    // default value:
    // TODO: boolean : false
    // TODO: int : 0
    // TODO: string : ""
    // TODO: list: []


    // TODO: -l -p 8080 -d /usr/logs
    @Test
    internal fun `命令行参数解析（单值）`() {
        val arguments = parse(MultiOptions::class, "-l", "-p", "8080", "-d", "/usr/logs")
        assertEquals(true, arguments.logging)
        assertEquals(8080, arguments.port)
        assertEquals("/usr/logs", arguments.directory)

    }

    @Test
    @Disabled
    internal fun `命令行参数解析（列表）`() {
        val arguments = parse(ListOptions::class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5")
        assertEquals(listOf("this", "is", "a", "list"), arguments.group)
        assertEquals(listOf(1, 2, -3, 5), arguments.numbers)

    }

    @Test
    internal fun `当有flag应该设置为true`() {
        val arguments = parse(BooleanOption::class, "-l")
        assertEquals(true, arguments.logging)
    }

    @Test
    internal fun `当没有flag应该设置为false`() {
        val arguments = parse(BooleanOption::class)
        assertEquals(false, arguments.logging)
    }

    @Test
    internal fun `当定义了一个int类型的option应该返回一个整数`() {
        val arguments = parse(IntOption::class, "-p", "8080")
        assertEquals(8080, arguments.port)
    }

    @Test
    internal fun `当定义了一个string类型的option应该返回一个字符串`() {
        val arguments = parse(StringOption::class, "-d", "/usr/logs")
        assertEquals("/usr/logs", arguments.directory)
    }
}

data class MultiOptions(@Option("l") val logging: Boolean, @Option("p") val port: Int, @Option("d") val directory: String)

data class ListOptions(@Option("g") val group: List<String>, @Option("d") val numbers: List<Int>)
data class BooleanOption(@Option("l") val logging: Boolean)
data class IntOption(@Option("p") val port: Int)
data class StringOption(@Option("d") val directory: String)
