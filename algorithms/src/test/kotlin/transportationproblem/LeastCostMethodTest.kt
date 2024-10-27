package transportationproblem

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LeastCostMethodTest : FunSpec({

    context("should find minimal transportation cost") {
        // given
        val tp = TransportationProblem(
            costs = arrayOf(
                intArrayOf(4, 6, 8, 8),
                intArrayOf(6, 8, 6, 7),
                intArrayOf(5, 7, 6, 8),
            ),
            shopsDemands = intArrayOf(20, 30, 50, 50),
            factorySupply = intArrayOf(40, 60, 50)
        )
        val leastCostMethod = LeastCostMethod(tp)

        // when
        val minCost = leastCostMethod.calculate()

        // then
        println("minCost = $minCost")
        leastCostMethod.table.printTable()

        leastCostMethod.table shouldBe arrayOf(
            intArrayOf(20, 20, 0, 0),
            intArrayOf(0, 0, 50, 10),
            intArrayOf(0, 10, 0, 40),
        )
        minCost shouldBe 960
    }

    context("should find minimal transportation cost 2") {
        // given
        val tp = TransportationProblem(
            costs = arrayOf(
                intArrayOf(11, 13, 17, 14),
                intArrayOf(16, 18, 14, 10),
                intArrayOf(21, 24, 13, 10),
            ),
            shopsDemands = intArrayOf(200, 225, 275, 250),
            factorySupply = intArrayOf(250, 300, 400)
        )
        val leastCostMethod = LeastCostMethod(tp)

        // when
        val minCost = leastCostMethod.calculate()

        // then
        println("minCost = $minCost")
        leastCostMethod.table.printTable()

        leastCostMethod.table shouldBe arrayOf(
            intArrayOf(200, 50, 0, 0),
            intArrayOf(0, 50, 0, 250),
            intArrayOf(0, 125, 275, 0),
        )
        minCost shouldBe 12825
    }
})
