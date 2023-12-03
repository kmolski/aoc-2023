fun main() {

    val digit = Regex("([0-9])")
    val digitOrWord = Regex("(?=([0-9]|one|two|three|four|five|six|seven|eight|nine))")

    fun part1(input: List<String>): Long {
        return input.sumOf { parseLine(it, digit, String::toLong) }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { parseLine(it, digitOrWord, String::digitOrWordToLong) }
    }

    val testInput = readInput("Day01.example")
    check(part1(testInput) == 142L)

    val testInput2 = readInput("Day01.example2")
    check(part2(testInput2) == 281L)

    val input = readInput("Day01.input")
    part1(input).println()
    part2(input).println()
}

fun parseLine(line: String, regex: Regex, parser: (String) -> Long): Long {
    var firstDigit: String? = null
    var lastDigit: String? = null
    for (match in regex.findAll(line)) {
        if (firstDigit == null) {
            firstDigit = match.groupValues[1]
        }
        lastDigit = match.groupValues[1]
    }
    return 10 * parser(firstDigit!!) + parser(lastDigit!!)
}

fun String.digitOrWordToLong(): Long {
    return when (this) {
        "one"   -> 1
        "two"   -> 2
        "three" -> 3
        "four"  -> 4
        "five"  -> 5
        "six"   -> 6
        "seven" -> 7
        "eight" -> 8
        "nine"  -> 9
        else -> this.toLong()
    }
}
