import kotlin.math.pow

fun main() {

    val cardRecord = Regex("Card(\\s+)(\\d+): (.*)")
    val numberList = Regex("([^|]+)\\|?")
    val whitespace = Regex("\\s+")

    data class Card(val id: Long, val winning: List<Long>, val hand: List<Long>) {
        fun matchingNumbers() = winning.count { hand.contains(it) }
        fun calculateScore() = 2.0.pow(matchingNumbers() - 1).toLong()
    }

    fun parseNumbers(match: MatchResult): List<Long> {
        return match.groupValues[1].trim().split(whitespace).map(String::toLong)
    }

    fun parseLine(line: String): Card {
        val matches = cardRecord.matchEntire(line)!!
        val numbers = numberList.findAll(matches.groupValues[3]).map(::parseNumbers).toList()
        return Card(matches.groupValues[2].toLong(), numbers[0], numbers[1])
    }

    fun part1(input: List<String>): Long {
        return input.map(::parseLine).sumOf(Card::calculateScore)
    }

    fun part2(input: List<String>): Long {
        val cardNumbers = input.map(::parseLine)
        val cards = cardNumbers.associateTo(HashMap()) { it.id to 1L }
        for (card in cardNumbers) {
            for (offset in 1..card.matchingNumbers()) {
                cards.computeIfPresent(card.id + offset) { _, count -> count + cards[card.id]!! }
            }
        }
        return cards.values.sum()
    }

    val testInput = readInput("Day04.example")
    check(part1(testInput) == 13L)
    check(part2(testInput) == 30L)

    val input = readInput("Day04.input")
    part1(input).println()
    part2(input).println()
}
