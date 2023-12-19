package promitech.ecc.async

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Predicate
import java.util.function.Supplier

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

    @Nested
    inner class TestRunInParallel {

        @Test
        fun `should run tasks`() {
            // given
            val s1 = Supplier<String> { "supplier 1" }
            val s2 = Supplier<String> { "supplier 2" }

            val jobRegistry = AsyncService.JobRegistry()
            val completableFuture1: CompletableFuture<String> = jobRegistry.registerWait(s1)
            val completableFuture2: CompletableFuture<String> = jobRegistry.registerWait(s2)

            // when
            asyncService.runInParallel(jobRegistry)

            // then
            assertThat(completableFuture1).isCompletedWithValue("supplier 1")
            assertThat(completableFuture2).isCompletedWithValue("supplier 2")
        }

        @Test
        @Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
        fun `should run tasks in parallel`() {
            // given
            val s1 = Supplier<String> { sleep(500); "supplier 1" }
            val s2 = Supplier<String> { sleep(500); "supplier 2" }
            val s3 = Supplier<String> { sleep(500); "supplier 3" }
            val s4 = Supplier<String> { sleep(500); "supplier 4" }

            val jobRegistry = AsyncService.JobRegistry()
            val completableFuture1: CompletableFuture<String> = jobRegistry.registerWait(s1)
            val completableFuture2: CompletableFuture<String> = jobRegistry.registerWait(s2)
            val completableFuture3: CompletableFuture<String> = jobRegistry.registerWait(s3)
            val completableFuture4: CompletableFuture<String> = jobRegistry.registerWait(s4)

            // when
            asyncService.runInParallel(jobRegistry)

            // then
            assertThat(completableFuture1).isCompletedWithValue("supplier 1")
            assertThat(completableFuture2).isCompletedWithValue("supplier 2")
            assertThat(completableFuture3).isCompletedWithValue("supplier 3")
            assertThat(completableFuture4).isCompletedWithValue("supplier 4")
        }

        @Test
        fun `should cache exception`() {
            // given
            val s1 = Supplier<String> { "supplier 1" }
            val s2 = Supplier<String> { throw RuntimeException("supplier 2 exception") }

            val jobRegistry = AsyncService.JobRegistry()
            val completableFuture1: CompletableFuture<String> = jobRegistry.registerWait(s1)
            val completableFuture2: CompletableFuture<String> = jobRegistry.registerWait(s2)

            // when
            asyncService.runInParallel(jobRegistry)

            // then
            assertThat(completableFuture1).isCompletedWithValue("supplier 1")
            assertThat(completableFuture2).isCompletedExceptionally
            var exception: Throwable? = null
            completableFuture2.exceptionally { t ->
                exception = t
                t.message
            }
            assertThat(exception).isInstanceOf(java.lang.RuntimeException::class.java)
                .hasMessage("supplier 2 exception")
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
