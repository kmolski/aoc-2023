fun main() {

    val partNumber = Regex("([0-9]+)")
    val gearSymbol = Regex("(\\*)")

    data class Token(val row: Int, val cols: IntRange, val value: String) {
        fun hasAdjacentOperator(schematic: List<String>): Boolean {
            for (col in cols) {
                for (colOffset in (-1..1)) {
                    for (rowOffset in (-1..1)) {
                        val colValue = schematic.getOrNull(row + rowOffset)?.getOrNull(col + colOffset)
                        if (colValue != '.' && colValue?.isDigit() == false) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun multiplyAdjacentTokens(partNumbers: List<Token>, partIdxMap: Array<IntArray>): Long {
            var partIdx = mutableSetOf<Int>()
            for (colOffset in (-1..1)) {
                for (rowOffset in (-1..1)) {
                    val colValue = partIdxMap.getOrNull(row + rowOffset)?.getOrNull(cols.first + colOffset)
                    if (colValue != null && colValue != -1) {
                        partIdx.add(colValue)
                    }
                }
            }
            return if (partIdx.count() == 2) {
                partIdx.map { partNumbers[it].value.toLong() }.reduce(Long::times)
            } else {
                0
            }
        }
    }

    fun getTokens(schematic: List<String>, tokenRegex: Regex): List<Token> {
        val tokens = mutableListOf<Token>()
        for ((index, line) in schematic.withIndex()) {
            for (match in tokenRegex.findAll(line)) {
                val tokenMatch = match.groups[1]!!
                val token = Token(index, tokenMatch.range, tokenMatch.value)
                tokens.add(token)
            }
        }
        return tokens
    }

    fun part1(input: List<String>): Long {
        return getTokens(input, partNumber).filter { it.hasAdjacentOperator(input) }.sumOf { it.value.toLong() }
    }

    fun part2(input: List<String>): Long {
        val partNumbers = getTokens(input, partNumber)
        val partIntMap = Array(200) { IntArray(200) { -1 } }
        for ((partIdx, part) in partNumbers.withIndex()) {
            for (col in part.cols) {
                partIntMap[part.row][col] = partIdx
            }
        }
        return getTokens(input, gearSymbol).sumOf { it.multiplyAdjacentTokens(partNumbers, partIntMap) }
    }

    val testInput = readInput("Day03.example")
    check(part1(testInput) == 4361L)
    check(part2(testInput) == 467835L)

    val input = readInput("Day03.input")
    part1(input).println()
    part2(input).println()
}
