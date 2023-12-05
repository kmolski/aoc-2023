fun main() {

    val cardRecord = Regex("Card (\\d+): (.*)")
    val subsetList = Regex("([^|]+)\\|?")
    val subsetElem = Regex("(\\d+) *")

    data class Token(val row: Int, val cols: IntRange, val value: String)

    fun part1(input: List<String>): Long {
        return 0L
    }

//    fun part2(input: List<String>): Long {
//    }

    val testInput = readInput("Day04.example")
    check(part1(testInput) == 4361L)
//    check(part2(testInput) == 467835L)

    val input = readInput("Day04.input")
    part1(input).println()
//    part2(input).println()
}
