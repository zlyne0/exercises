package coinschangeproblem

data class CoinsChangeResult(val coinsCount: Int, val coinsAmounts: IntArray)

fun calculateCoinsChange(coins: IntArray, maxSum: Int): CoinsChangeResult {

    val coinAmounts = IntArray(coins.size) { _ -> 0 }
    val theBestCoinsAmounts = IntArray(coins.size) { _ -> 0 }
    var theBestCoinsCount = Integer.MAX_VALUE

    while (true) {
        val s = sum(coins, coinAmounts)
        if (s == maxSum) {
            printCoins(coins, coinAmounts)
            val coinsCount = coinsCount(coinAmounts)
            if (coinsCount < theBestCoinsCount) {
                theBestCoinsCount = coinsCount
                coinAmounts.copyInto(theBestCoinsAmounts)
            }
        }
        if (s < maxSum) {
            coinAmounts[0]++
            continue
        }

        // increase amount of bigger coin
        for (j in 0 until coinAmounts.size-1) {
            coinAmounts[j] = 0
            coinAmounts[j+1]++
            if (sum(coins, coinAmounts) <= maxSum) {
                break
            }
        }
        // main loop break, the biggest coin sum larger then sum
        if (coinAmounts[coins.size-1] * coins[coins.size-1] > maxSum) {
            break;
        }
    }
    return CoinsChangeResult(theBestCoinsCount, theBestCoinsAmounts)
}

fun coinsCount(coinsAmounts: IntArray): Int {
    var count = 0
    for (i in 0 until coinsAmounts.size) {
        count += coinsAmounts[i]
    }
    return count
}

fun sum(coins: IntArray, coinAmounts: IntArray): Int {
    var s = 0
    for (i in 0 until coinAmounts.size) {
        s += coins[i] * coinAmounts[i]
    }
    return s
}

fun coinsToStr(coins: IntArray, coinAmounts: IntArray): String {
    var s = ""
    for (i in 0 until coinAmounts.size) {
        if (coinAmounts[i] > 0) {
            for (ic in 0 until coinAmounts[i]) {
                if (s.isNotBlank()) {
                    s += " "
                }
                s += coins[i].toString()
            }
        }
    }
    return s
}

fun printCoins(coins: IntArray, coinAmounts: IntArray) {
    println(coinsToStr(coins, coinAmounts))
}
