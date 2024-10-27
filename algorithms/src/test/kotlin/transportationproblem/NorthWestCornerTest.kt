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
        northWestCorner.table.printTable()

        minCost shouldBe 12200
        northWestCorner.table shouldBe arrayOf(
            intArrayOf(200, 50, 0, 0),
            intArrayOf(0, 175, 125, 0),
            intArrayOf(0, 0, 150, 250),
        )
    }

    context("should find minimal transportation cost 2") {
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
        val northWestCorner = NorthWestCorner(tp)

        // when
        val minCost = northWestCorner.calculate()

        // then
        println("minCost = $minCost")
        northWestCorner.table.printTable()

        minCost shouldBe 980
        northWestCorner.table shouldBe arrayOf(
            intArrayOf(20, 20, 0, 0),
            intArrayOf(0, 10, 50, 0),
            intArrayOf(0, 0, 0, 50),
        )
    }
})
