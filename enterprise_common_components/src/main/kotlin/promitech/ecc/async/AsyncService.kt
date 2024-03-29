package promitech.ecc.async

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Supplier

class AsyncService(
    private val threadPoolNumber: Int = 10,
    private val executorThreadPool: ExecutorService = Executors.newFixedThreadPool(threadPoolNumber)
) {

    fun <OUT> async(action: () -> OUT): CompletableFuture<OUT> {
        return CompletableFuture<OUT>().completeAsync({ action() }, executorThreadPool)
    }

    fun <IN, OUT> async(inputData: IN, action: (IN) -> OUT): CompletableFuture<OUT> {
        return CompletableFuture<OUT>().completeAsync({ action(inputData) }, executorThreadPool)
    }

    fun <IN, OUT> runInParallel(inputDataList: List<Pair<IN, (IN) -> OUT>>): List<Pair<IN, Result<OUT>>> {
        return inputDataList
            .map { inputData -> createCompletableFuture(inputData.first, inputData.second) }
            .map { inputAndCompFuture -> mapCompletableFuture(inputAndCompFuture) }
    }

    fun <IN, OUT> runInParallel(inputDataList: List<IN>, action: (IN) -> OUT): List<Pair<IN, Result<OUT>>> {
        return inputDataList
            .map { inputData -> createCompletableFuture(inputData, action) }
            .map(::mapCompletableFuture)
    }

    private fun <IN, OUT> createCompletableFuture(inputData: IN, action: (IN) -> OUT): Pair<IN, CompletableFuture<OUT>> {
        val completableFuture = CompletableFuture<OUT>().completeAsync({ action.invoke(inputData) }, executorThreadPool)
        return Pair(inputData, completableFuture)
    }

    private fun <IN, OUT> mapCompletableFuture(inputAndCompFuture: Pair<IN, CompletableFuture<OUT>>): Pair<IN, Result<OUT>> {
        try {
            val out: OUT = inputAndCompFuture.second.join()
            return Pair(inputAndCompFuture.first, Result.success(out))
        } catch (exception: CompletionException) {
            if (exception.cause != null) {
                return Pair(inputAndCompFuture.first, Result.failure(exception.cause!!) )
            } else {
                return Pair(inputAndCompFuture.first, Result.failure(exception) )
            }
        } catch (exception: RuntimeException) {
            return Pair(inputAndCompFuture.first, Result.failure(exception) )
        }
    }

    fun runInParallel(jobRegistry: JobRegistry) {
        for (pair in jobRegistry.pairs) {
            val supplier = pair.first
            val completableFuture = pair.second
            executorThreadPool.submit {
                try {
                    completableFuture.complete(supplier.get())
                } catch (exception: RuntimeException) {
                    completableFuture.completeExceptionally(exception)
                }
            }
        }
        for (pair in jobRegistry.pairs) {
            val completableFuture = pair.second
            try {
                completableFuture.join()
            } catch (completionException: CompletionException) {
                println(completionException)
            }
        }
    }

    class JobRegistry {
        internal val pairs = ArrayList<Pair<Supplier<Any>, CompletableFuture<Any>>>()

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> registerWait(supplier: Supplier<T>): CompletableFuture<T> {
            val completableFuture = CompletableFuture<Any>()
            pairs.add(Pair(supplier as Supplier<Any>, completableFuture))
            return completableFuture as CompletableFuture<T>
        }
    }
}