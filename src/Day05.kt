interface Mapping<T> {
    val input: String

    fun apply(number: List<T>): List<T>
}

fun main() {

    data class MappingRange(val range: LongRange, val offset: Long)
    data class LongMapping(override val input: String, val ranges: List<MappingRange>) : Mapping<Long> {
        fun applyMapping(mapping: MappingRange, number: Long): Long? {
            return if (number in mapping.range) { number + mapping.offset } else { null }
        }

        override fun apply(number: List<Long>): List<Long> {
            return number.map {
                ranges.firstNotNullOfOrNull { mapping -> applyMapping(mapping, it) } ?: it
            }
        }
    }

    data class LongRangeMapping(override val input: String, val ranges: List<MappingRange>) : Mapping<LongRange> {
        fun applyMapping(mapping: MappingRange, number: LongRange): LongRange? {
            return if (mapping.range.contains(number)) { number + mapping.offset } else { null }
        }

        override fun apply(number: List<LongRange>): List<LongRange> {
            var cuts = number
            for (range in ranges) {
                cuts = cuts.flatMap { it.cut(range.range) }
            }
            return cuts.map {
                ranges.firstNotNullOfOrNull { mapping -> applyMapping(mapping, it) } ?: it
            }
        }
    }

    data class Almanac<T>(val seeds: List<T>, val maps: Map<String, Mapping<T>>) {
        fun computeCategory(outputCategory: String): List<T> {
            if (outputCategory == "seed") {
                return seeds
            } else {
                val mapping = maps[outputCategory]!!
                return mapping.apply(computeCategory(mapping.input))
            }
        }
    }

    fun readRangeMapping(range: String): MappingRange {
        val numbers = range.split(" ")
        val outStart = numbers[0].toLong()
        val inStart = numbers[1].toLong()
        val length = numbers[2].toLong()
        return MappingRange(inStart..<inStart + length, outStart - inStart)
    }

    fun readAlmanac(chunks: List<String>): Almanac<Long> {
        val seeds = chunks[0].split(":")[1].trim().split(" ").map(String::toLong)
        val maps = mutableMapOf<String, LongMapping>()
        for (map in chunks.drop(1)) {
            val lines = map.trim().lines()
            val input = lines[0].split("-")[0]
            val output = lines[0].split("-")[2].split(" ")[0]
            val ranges = mutableListOf<MappingRange>()
            for (mapping in lines.drop(1)) {
                ranges.add(readRangeMapping(mapping))
            }
            maps[output] = LongMapping(input, ranges)
        }
        return Almanac(seeds, maps)
    }

    fun part1(input: List<String>): Long = readAlmanac(input).computeCategory("location").min()

    fun transformAlmanac(almanac: Almanac<Long>): Almanac<LongRange> {
        val seedRanges = almanac.seeds.chunked(2).map { it[0]..<it[0] + it[1] }
        val maps = almanac.maps.map { entry ->
            val mapping = entry.value as LongMapping
            entry.key to LongRangeMapping(mapping.input, mapping.ranges)
        }.toMap()
        return Almanac(seedRanges, maps)
    }

    fun part2(input: List<String>): Long {
        return transformAlmanac(readAlmanac(input)).computeCategory("location").minOf(LongRange::start)
    }

    val testInput = readInputChunks("Day05.example")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInputChunks("Day05.input")
    part1(input).println()
    part2(input).println()
}
