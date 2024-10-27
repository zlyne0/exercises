package transportationproblem

fun Array<IntArray>.printTable() {
    for (irow in 0 until this.size) {
        for (icol in 0 until this[irow].size) {
            val v = this[irow][icol]
            val s = if (v != -1) {
                v.toString()
            } else {
                "."
            }
            print(" " + s)
        }
        println()
    }
}
