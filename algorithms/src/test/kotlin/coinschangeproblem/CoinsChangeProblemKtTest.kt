package coinschangeproblem

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class CoinsChangeProblemKtTest : FunSpec({

    context("should calculate coins change") {
        data class TD(val maxSum: Int, val coins: List<Int>, val coinsAmount: Int, val coinsValues: String): WithDataTestName {
            override fun dataTestName(): String {
                return "maxSum: $maxSum, coins: $coins, coinsAmount: $coinsAmount, coinsValues: \"$coinsValues\""
            }
        }

        withData(
            TD(13, listOf(1, 2, 5, 10), 3, "1 2 10"),
            TD(6, listOf(1, 3, 4), 2, "3 3")
        ) { (maxSum, coins, allCoinsAmount, allCoinsValues) ->
            val (coinsCount,  coinsAmounts) = calculateCoinsChange(coins.toIntArray(), maxSum)
            coinsCount shouldBe allCoinsAmount
            allCoinsValues shouldBe coinsToStr(coins.toIntArray(), coinsAmounts)
        }
    }

})
