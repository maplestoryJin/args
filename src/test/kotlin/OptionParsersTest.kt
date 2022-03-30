import exception.IllegalValueException
import exception.InsufficientArgumentsException
import exception.TooManyArgumentsException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.*

class OptionParsersTest {

    @Nested
    class SingleValuedParserTest {
        @Test
        internal fun `不接受额外参数`() {
            val exception = assertThrows<TooManyArgumentsException> {
                unary(0) { it.toInt() }.parse(arrayOf("-p", "8080", "8081"), option("p"))
            }
            assertEquals("p", exception.option)
        }

        @Test
        internal fun 参数的数据格式有问题时应该抛出异常() {
            val exception = assertThrows<IllegalValueException> {
                unary(0) { it.toInt() }.parse(arrayOf("-p", "s"), option("p"))
            }
            assertEquals("s", exception.value)
            assertEquals("p", exception.option)
        }

        @ParameterizedTest
        @ValueSource(strings = ["-p", "-p -d"])
        fun `当参数不充分的时候应该抛出异常`(arguments: String) {
            val exception = assertThrows<InsufficientArgumentsException> {
                unary(0) { it.toInt() }.parse(arguments.split(" ").toTypedArray(), option("p"))
            }
            assertEquals("p", exception.option)
        }

        @Test
        internal fun `当flag不存在的时候应该返回默认值`() {
            val whatever = Any()
            assertSame(whatever, unary(whatever) {}.parse(arrayOf(), option("p")))
        }

        @Test
        internal fun `当定义了一个singleValue类型的option应该解析value`() {
            val parsed = Any()
            val whatever = Any()
            val parse = unary(whatever) { parsed }.parse(arrayOf("-d", "/usr/logs"), option("d"))
            assertSame(parsed, parse)
        }
    }

    @Nested
    class BooleanOptionParserTest {

        @Test
        internal fun `不接受额外的参数`() {
            val exception = assertThrows<TooManyArgumentsException> {
                bool().parse(arrayOf("-l", "t"), option("l"))
            }

            assertEquals("l", exception.option)

        }

        @Test
        internal fun `如果不存在flag应该设置默认值`() {
            assertFalse(bool().parse(arrayOf(), option("l")))

        }

        @Test
        internal fun `如果存在flag应该设置为true`() {
            assertTrue(bool().parse(arrayOf("-l"), option("l")))
        }

    }

    @Nested
    class ListOptionParserTest {
        @Test
        internal fun 解析array列表() {
            assertContentEquals(
                arrayOf("this", "is"),
                list(arrayOf()) { it }.parse(arrayOf("-g", "this", "is"), option("g"))
            )
        }

        @Test
        internal fun 默认值() {
            assertContentEquals(
                arrayOf(1, 2, 3),
                list(arrayOf(1, 2, 3)) { it.toInt() }.parse(arrayOf(), option("g"))
            )
        }

        @Test
        internal fun 应该抛出异常当数据格式有误() {
            val exception = assertThrows<IllegalValueException> {
                list(arrayOf()) { throw RuntimeException() }.parse(arrayOf("-g", "this", "is"), option("g"))
            }

            assertEquals("this", exception.value)
            assertEquals("g", exception.option)
        }
    }
}

private fun option(value: String): Option = Option(value)
