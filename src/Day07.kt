enum class HandType {
    FIVE_OF_KIND {
        override fun matches(freqs: Map<Char, Int>) = freqs.containsValue(5)
    },
    FOUR_OF_KIND {
        override fun matches(freqs: Map<Char, Int>) = freqs.containsValue(4)
    },
    FULL_HOUSE {
        override fun matches(freqs: Map<Char, Int>) = freqs.containsValue(3) && freqs.containsValue(2)
    },
    THREE_OF_KIND {
        override fun matches(freqs: Map<Char, Int>) = freqs.containsValue(3)
    },
    TWO_PAIR {
        override fun matches(freqs: Map<Char, Int>) = freqs.filterValues { it == 2 }.count() == 2
    },
    ONE_PAIR {
        override fun matches(freqs: Map<Char, Int>) = freqs.containsValue(2)
    },
    HIGH_CARD;

    open fun matches(freqs: Map<Char, Int>) = true
}

data class Hand(val cards: List<Char>, val bid: Long) {
    private val freqs = cards.associateWith { key -> cards.count { it == key } }
    private fun freqsWithJoker(): Map<Char, Int> {
        val replaced = freqs.filterKeys { it != 'J' }.toMutableMap()
        replaced.computeIfPresent(replaced.maxBy { it.value }.key) { _, v -> v + (freqs['J'] ?: 0) }
        return replaced
    }

    fun getType(joker: Boolean): HandType {
        return HandType.entries.first { it.matches(freqs) || (joker && it.matches(freqsWithJoker())) }
    }
}

fun main() {

    fun parseInput(input: List<String>): List<Hand> {
        return input.map {
            val tokens = it.split(" ")
            Hand(tokens[0].toList(), tokens[1].toLong())
        }
    }

    fun handComparator(cardStrength: (Char) -> Int, joker: Boolean): Comparator<Hand> {
        return compareBy<Hand> { it.getType(joker) }
            .thenComparator { a, b -> a.cards.zip(b.cards)
                .map { compareValues(cardStrength(it.second), cardStrength(it.first)) }
                .find { it != 0 }!!
            }
    }

    fun solve(input: List<String>, cardStrength: (Char) -> Int, joker: Boolean): Long {
        return parseInput(input)
            .sortedWith(handComparator(cardStrength, joker)).reversed()
            .withIndex().sumOf { (rank, hand) -> (rank + 1) * hand.bid }
    }

    fun strength(card: Char): Int {
        return when (card) {
            in '2'..'9' -> card.digitToInt()
            'T' -> 10
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> 0
        }
    }

    fun strengthJoker(card: Char): Int {
        return when (card) {
            'J' -> 1
            in '2'..'9' -> card.digitToInt()
            'T' -> 10
            'Q' -> 11
            'K' -> 12
            'A' -> 13
            else -> 0
        }
    }

    fun part1(input: List<String>) = solve(input, ::strength, false)
    fun part2(input: List<String>) = solve(input, ::strengthJoker, true)

    val testInput = readInput("Day07.example")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07.input")
    part1(input).println()
    part2(input).println()
}
