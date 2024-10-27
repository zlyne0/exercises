package transportationproblem

class NorthWestCorner(
    private val tp: TransportationProblem
) {
    val table: Array<IntArray> = Array(tp.factoryCount) { IntArray(tp.shopsCount) { -1 } }
    val shopsDemands: IntArray = tp.shopsDemands.clone()
    val factorySupply: IntArray = tp.factorySupply.clone()

    fun calculate(): Int {
        for (row in 0 until tp.factoryCount) {
            for (col in 0 until tp.shopsCount) {
                val tableVal = table[row][col]
                if (tableVal == -1) {
                    if (factorySupply[row] == shopsDemands[col]) {
                        table[row][col] = shopsDemands[col]
                        shopsDemands[col] = 0
                        factorySupply[row] = 0
                        markNoMoreDemands(col)
                        markNoMoreSupply(row)
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
            }
        }
        return sumCost()
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
}
