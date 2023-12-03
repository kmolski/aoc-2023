import kotlin.math.max

fun main() {

    val gameRecord = Regex("Game (\\d+): (.*)")
    val subsetList = Regex("([^;]+);?")
    val subsetElem = Regex("(\\d+) (\\w+),?")

    data class Subset(val cubeCounts: Map<String, Long>)
    data class Game(val id: Long, val subsets: List<Subset>) {
        fun cubeCountGreaterThan(cubeCounts: Map<String, Long>): Boolean {
            return subsets.all { subset ->
                cubeCounts.all { (cubeType, cubeCount) ->
                    subset.cubeCounts[cubeType]?.let { it <= cubeCount } ?: true
                }
            }
        }
    }

    fun parseSubset(match: MatchResult): Subset {
        val subset = match.groupValues[1]
        val cubeCounts = mutableMapOf<String, Long>()
        for (count in subsetElem.findAll(subset)) {
            val cubeCount = count.groupValues[1].toLong()
            val cubeType = count.groupValues[2]
            cubeCounts.merge(cubeType, cubeCount, ::max)
        }
        return Subset(cubeCounts)
    }

    fun parseLine(line: String): Game {
        val matches = gameRecord.matchEntire(line)!!
        val rounds = matches.groupValues[2]
        val subsets = subsetList.findAll(rounds).map(::parseSubset).toList()
        return Game(matches.groupValues[1].toLong(), subsets)
    }

    fun part1(input: List<String>): Long {
        return input.map(::parseLine)
            .filter { it.cubeCountGreaterThan(mapOf("red" to 12L, "green" to 13L, "blue" to 14L)) }
            .sumOf(Game::id)
    }

    fun part2(input: List<String>): Long {
        return input.map(::parseLine).sumOf { game ->
            sequenceOf("red", "green", "blue").map { type ->
                game.subsets.mapNotNull { it.cubeCounts[type] }.max()
            }
            .reduce(Long::times)
        }
    }

    val testInput = readInput("Day02.example")
    check(part1(testInput) == 8L)
    check(part2(testInput) == 2286L)

    val input = readInput("Day02.input")
    part1(input).println()
    part2(input).println()
}
