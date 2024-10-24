package transportationproblem

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class NorthWestCornerTest : FunSpec({

    context("should find minimal transportation cost") {
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
        val northWestCorner = NorthWestCorner(tp)

        // when
        val minCost = northWestCorner.calculate()

        // then
        println("minCost = $minCost")
        northWestCorner.printTable()

        minCost shouldBe 12200
        northWestCorner.table shouldBe arrayOf(
            intArrayOf(200, 50, 0, 0),
            intArrayOf(0, 175, 125, 0),
            intArrayOf(0, 0, 150, 250),
        )
    }
})
