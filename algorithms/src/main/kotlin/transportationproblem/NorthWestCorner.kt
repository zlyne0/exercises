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
                        zeroForShops(col, row)
                        zeroForFactories(row, col)
                    } else if (factorySupply[row] > shopsDemands[col]) {
                        table[row][col] = shopsDemands[col]
                        factorySupply[row] -= shopsDemands[col]
                        shopsDemands[col] = 0
                        zeroForShops(col, row)
                    } else if (shopsDemands[col] > factorySupply[row]) {
                        table[row][col] = factorySupply[row]
                        shopsDemands[col] -= factorySupply[row]
                        factorySupply[row] = 0
                        zeroForFactories(row, col)
                    }
                }
            }
        }
        var costSum = 0
        for (row in 0 until tp.factoryCount) {
            for (col in 0 until tp.shopsCount) {
                val v = table[row][col]
                if (v > 0) {
                    costSum += v * tp.costs[row][col]
                }
            }
        }
        return costSum
    }

    private fun zeroForShops(col: Int, fromRow: Int) {
        for (i in fromRow + 1 until tp.factoryCount) {
            table[i][col] = 0
        }
    }

    private fun zeroForFactories(row: Int, fromCol: Int) {
        for (i in fromCol + 1 until tp.shopsCount) {
            table[row][i] = 0
        }
    }

    fun printTable() {
        for (irow in 0 until tp.factoryCount) {
            for (icol in 0 until tp.shopsCount) {
                val v = table[irow][icol]
                val s = if (v != -1) {
                    v.toString()
                } else {
                    " "
                }
                print(" " + s)
            }
            println()
        }
    }
}
