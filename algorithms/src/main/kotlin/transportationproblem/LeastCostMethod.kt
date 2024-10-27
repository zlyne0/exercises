package transportationproblem

class LeastCostMethod(val tp: TransportationProblem) {

    val table: Array<IntArray> = Array(tp.factoryCount) { IntArray(tp.shopsCount) { -1 } }
    val shopsDemands: IntArray = tp.shopsDemands.clone()
    val factorySupply: IntArray = tp.factorySupply.clone()

    private var row: Int = 0
    private var col: Int = 0

    fun calculate(): Int {
        while (findLeastCost()) {
            if (factorySupply[row] == shopsDemands[col]) {
                table[row][col] = shopsDemands[col]
                shopsDemands[col] = 0
                factorySupply[row] = 0
                markNoMoreSupply(row)
                markNoMoreDemands(col)
            } else if (factorySupply[row] > shopsDemands[col]) {
                table[row][col] = shopsDemands[col]
                factorySupply[row] -= shopsDemands[col]
                shopsDemands[col] = 0
                markNoMoreDemands(col)
            } else if (shopsDemands[col] > factorySupply[row]) {
                table[row][col] = factorySupply[row]
                shopsDemands[col] -= factorySupply[row]
                factorySupply[row] = 0
                markNoMoreSupply(row)
            }
        }
        return sumCost()
    }

    private fun markNoMoreDemands(sourceCol: Int) {
        for (i in 0 until table.size) {
            if (table[i][sourceCol] == -1) {
                table[i][sourceCol] = 0
            }
        }
    }

    private fun markNoMoreSupply(sourceRow: Int) {
        for (i in 0 until table[sourceRow].size) {
            if (table[sourceRow][i] == -1) {
                table[sourceRow][i] = 0
            }
        }
    }

    private fun findLeastCost(): Boolean {
        var minCost = Int.MAX_VALUE
        var betterDemand = 0
        var found = false

        for (irow in 0 until tp.factoryCount) {
            for (icol in 0 until tp.shopsCount) {
                if (table[irow][icol] >= 0) {
                    continue
                }
                if (tp.costs[irow][icol] < minCost) {
                    minCost = tp.costs[irow][icol]
                    betterDemand = tp.shopsDemands[icol]
                    row = irow
                    col = icol
                    found = true
                } else if (tp.costs[irow][icol] == minCost) {
                    if (tp.shopsDemands[icol] > betterDemand) {
                        minCost = tp.costs[irow][icol]
                        betterDemand = tp.shopsDemands[icol]
                        row = irow
                        col = icol
                        found = true
                    }
                }
            }
        }
        return found
    }

    private fun sumCost(): Int {
        var costSum = 0
        for (row in 0 until table.size) {
            for (col in 0 until table[row].size) {
                val v = table[row][col]
                if (v > 0) {
                    costSum += v * tp.costs[row][col]
                }
            }
        }
        return costSum
    }
}