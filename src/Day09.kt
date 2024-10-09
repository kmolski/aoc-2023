fun main() {

    fun parseInput(name: String) = readInput(name).map {
        it.split(" ").map(String::toLong)
    }

    fun derive(sequence: List<Long>): List<List<Long>> {
        val derivatives: MutableList<List<Long>> = mutableListOf()
        var currentDrv = sequence.toMutableList()
        while (currentDrv.any { it != 0L }) {
            derivatives.add(currentDrv)
            currentDrv = currentDrv.windowed(2).map { it[1] - it[0] }.toMutableList()
        }
        return derivatives
    }

    fun extrapolateForward(derivations: List<List<Long>>): Long {
        val extraValues = (1..derivations.size).map { derivations.last().last() }.toMutableList()
        for (i in derivations.size - 1 downTo 1) {
            extraValues[i - 1] = (derivations[i - 1].last() + extraValues[i])
        }
        return extraValues.first()
    }

    fun extrapolateBack(derivations: List<List<Long>>): Long {
        val extraValues = (1..derivations.size).map { derivations.last().first() }.toMutableList()
        for (i in derivations.size - 1 downTo 1) {
            extraValues[i - 1] = (derivations[i - 1].first() - extraValues[i])
        }
        return extraValues.first()
    }

    fun part1(derivatives: List<List<List<Long>>>) = derivatives.map(::extrapolateForward).sum()
    fun part2(derivatives: List<List<List<Long>>>) = derivatives.map(::extrapolateBack).sum()

    val testInput = parseInput("Day09.example")
    val testDerivatives = testInput.map(::derive)
    check(part1(testDerivatives) == 114L)
    check(part2(testDerivatives) == 2L)

    val input = parseInput("Day09.input")
    val inputDerivatives = input.map(::derive)
    part1(inputDerivatives).println()
    part2(inputDerivatives).println()
}
