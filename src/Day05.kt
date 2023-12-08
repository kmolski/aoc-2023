fun main() {

    operator fun LongRange.plus(offset: Long): LongRange {
        return (first + offset)..(last + offset)
    }

    data class RangeMapping(val range: LongRange, val offset: Long)
    data class Mapping(val input: String, val ranges: List<RangeMapping>) {
        fun apply(input: List<Long>): List<Long> {
            return input.map {
                ranges.find { mapping -> it in mapping.range }?.let { mapping -> it + mapping.offset } ?: it
            }
        }
    }

    data class Almanac(val seeds: List<Long>, val maps: Map<String, Mapping>) {
        fun computeCategory(outputCategory: String): List<Long> {
            if (outputCategory == "seed") {
                return seeds
            } else {
                val mapping = maps[outputCategory]!!
                return mapping.apply(computeCategory(mapping.input))
            }
        }
    }

    fun readRangeMapping(range: String): RangeMapping {
        val numbers = range.split(" ")
        val outStart = numbers[0].toLong()
        val inStart = numbers[1].toLong()
        val length = numbers[2].toLong()
        return RangeMapping(inStart..(inStart + length), outStart - inStart)
    }

    fun readAlmanac(chunks: List<String>): Almanac {
        val seeds = chunks[0].split(":")[1].trim().split(" ").map(String::toLong)
        val maps = mutableMapOf<String, Mapping>()
        for (map in chunks.drop(1)) {
            val lines = map.trim().lines()
            val input = lines[0].split("-")[0]
            val output = lines[0].split("-")[2].split(" ")[0]
            val ranges = mutableListOf<RangeMapping>()
            for (mapping in lines.drop(1)) {
                ranges.add(readRangeMapping(mapping))
            }
            maps[output] = Mapping(input, ranges)
        }
        return Almanac(seeds, maps)
    }

    fun part1(input: List<String>): Long {
        return readAlmanac(input).computeCategory("location").min()
    }

    fun part2(input: List<String>): Long {
        val almanac = readAlmanac(input)
        return almanac.computeCategory("location").min()
    }

    val testInput = readInputChunks("Day05.example")
    check(part1(testInput) == 35L)
//    check(part2(testInput) == 46L)

    val input = readInputChunks("Day05.input")
    part1(input).println()
//    part2(input).println()
}
