package promitech.ecc.async

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import java.util.function.Predicate

class AsyncServiceTest {

    val asyncService = AsyncService()

    @Nested
    inner class RunAsync {

        @Test
        fun should_run_in_async() {
            // given

            // when
            val completableFuture = asyncService.async("one", { str: String -> str + "." + str })
            val output = completableFuture.get()

            // then
            assertThat(output).isEqualTo("one.one")
        }
    }

    @Nested
    inner class RunInParallelForSingleActionTest {

        @Test
        fun `should execute action on each data list elements`() {
            // given
            val dataList = listOf("one", "two", "three")
            val action: (String) -> String = { str ->
                str + "." + str
            }

            // when
            val result = asyncService.runInParallel(dataList, action)

            // then
            assertThat(result)
                .hasSize(3)
                .anyMatch(has("one", "one.one"))
                .anyMatch(has("two", "two.two"))
                .anyMatch(has("three", "three.three"))
        }

        @Test
        @Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
        fun `should execute action on data parallel`() {
            // given
            val dataList = listOf("one", "two", "three", "four", "five")
            val action: (String) -> String = { str ->
                sleep(500)
                str + "." + str
            }

            // when
            val result = asyncService.runInParallel(dataList, action)

            // then
            assertThat(result).hasSize(5)
        }

        @Test
        fun `should execute action and cache exception`() {
            // given
            val dataList = listOf("one", "two", "three")
            val action: (String) -> String = { str ->
                if (str.equals("two")) {
                    throw RuntimeException("test exception")
                } else {
                    str + "." + str
                }
            }

            // when
            val result = asyncService.runInParallel(dataList, action)

            // then
            assertThat(result)
                .hasSize(3)
                .anyMatch(has("one", "one.one"))
                .anyMatch(hasException("two", "test exception"))
                .anyMatch(has("three", "three.three"))
        }
    }

    @Nested
    inner class RunInParallelForMultipleActionsTest {

        @Test
        fun `should execute action on each data list elements`() {
            // given
            val inputList = listOf(
                Pair("one", { str: String -> str + "1" + str } ),
                Pair("two", { str: String -> str + "2" + str } ),
                Pair("three", { str: String -> str + "3" + str } )
            )

            // when
            val result = asyncService.runInParallel(inputList)

            // then
            assertThat(result)
                .hasSize(3)
                .anyMatch(has("one", "one1one"))
                .anyMatch(has("two", "two2two"))
                .anyMatch(has("three", "three3three"))
        }

        @Test
        @Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
        fun `should execute action on data parallel`() {
            // given
            val inputList = listOf(
                Pair("one", { str: String -> sleep(500); str + "1" + str } ),
                Pair("two", { str: String -> sleep(500); str + "2" + str } ),
                Pair("three", { str: String -> sleep(500); str + "3" + str } ),
                Pair("four", { str: String -> sleep(500); str + "4" + str } ),
                Pair("five", { str: String -> sleep(500); str + "5" + str } )
            )

            // when
            val result = asyncService.runInParallel(inputList)

            // then
            assertThat(result).hasSize(5)
        }

        @Test
        fun `should execute action and cache exception`() {
            // given
            val inputList = listOf(
                Pair("one", { str: String -> str + "1" + str } ),
                Pair("two", { str: String ->
                    if (str.equals("two")) {
                        throw RuntimeException("test exception")
                    } else {
                        str + "2" + str
                    }
                } ),
                Pair("three", { str: String -> str + "3" + str } )
            )

            // when
            val result = asyncService.runInParallel(inputList)

            // then
            assertThat(result)
                .hasSize(3)
                .anyMatch(has("one", "one1one"))
                .anyMatch(hasException("two", "test exception"))
                .anyMatch(has("three", "three3three"))
        }
    }

    companion object {
        fun has(keyStr: String, value: String): Predicate<Pair<String, Result<String>>> =
            Predicate<Pair<String, Result<String>>> { t ->
                keyStr.equals(t.first) && t.second.isSuccess &&  value.equals(t.second.getOrNull())
            }

        fun hasException(keyStr: String, exceptionMessage: String): Predicate<Pair<String, Result<String>>> =
            Predicate<Pair<String, Result<String>>> { t ->
                keyStr.equals(t.first) && t.second.isFailure &&  exceptionMessage.equals(t.second.exceptionOrNull()?.message)
            }

        fun sleep(mili: Long) {
            Thread.sleep(mili)
        }
    }

}
