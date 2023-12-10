import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToLong
import kotlin.math.sqrt

fun main() {

    val inputLine = Regex("\\s*(\\d+)")

    fun parseLine(input: String): List<Long> {
        return inputLine.findAll(input.split(":")[1]).map { it.groupValues[1].toLong() }.toList()
    }

    fun parseInput(input: List<String>): List<Pair<Long, Long>> {
        val times = parseLine(input[0])
        val distances = parseLine(input[1])
        return times.zip(distances)
    }

    fun delta(totalTime: Long, distanceToBeat: Long): Double {
        return sqrt((totalTime * totalTime - 4 * distanceToBeat).toDouble())
    }

    fun countWinners(totalTime: Long, distanceToBeat: Long): Long {
        // round X.00-X.99(9) to X+1
        val x1 = floor((totalTime - delta(totalTime, distanceToBeat)) / 2 + 1).roundToLong()
        // round X.00-X.99(9) to X-1
        val x2 = ceil((totalTime + delta(totalTime, distanceToBeat)) / 2 - 1).roundToLong()
        return x2 - x1 + 1
    }

    fun part1(input: List<String>): Long {
        return parseInput(input).map { race -> countWinners(race.first, race.second) }.reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        return part1(input.map { it.replace(" ", "") })
    }

    val testInput = readInput("Day06.example")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06.input")
    part1(input).println()
    part2(input).println()
}
