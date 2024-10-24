package transportationproblem

class TransportationProblem(
    val costs: Array<IntArray>,
    val shopsDemands: IntArray,
    val factorySupply: IntArray
) {
    val shopsCount = shopsDemands.size
    val factoryCount = factorySupply.size

    init {
        require(shopsDemands.sum() == factorySupply.sum()) { "sum of supply and demand should be equals" }
        require(factoryCount == costs.size) { "require factory amount equals costs row size" }
        require(shopsCount == costs[0].size) { "require shops amount equals costs column size" }
    }
}