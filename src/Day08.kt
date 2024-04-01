fun main() {

    val inputLine = Regex("(\\w+) = \\((\\w+), (\\w+)\\)")
    data class Node(val key: String, val left: String, val right: String)

    fun parseInput(name: String): Pair<Sequence<Char>, Map<String, Node>> {
        val input = readInputChunks(name)
        val directions = input[0].asSequence().repeat()
        val nodes = mutableMapOf<String, Node>()
        for (line in input[1].lineSequence().filter(String::isNotEmpty)) {
            val matches = inputLine.matchEntire(line)!!.groupValues
            val node = Node(matches[1], matches[2], matches[3])
            nodes[node.key] = node
        }
        return Pair(directions, nodes)
    }

    fun walk(directions: Sequence<Char>, nodes: Map<String, Node>, start: Regex, end: Regex): Long {
        val startNodes = nodes.values.filter { it.key.matches(start) }
        val steps = startNodes.map {
            var current = it
            var stepCount = 0L
            val dirs = directions.iterator()
            while (!current.key.matches(end)) {
                current = nodes[if (dirs.next() == 'L') { current.left } else { current.right }]!!
                ++stepCount
            }
            stepCount
        }
        return steps.reduce(Long::lcm)
    }

    fun part1(directions: Sequence<Char>, nodes: Map<String, Node>): Long {
        return walk(directions, nodes, Regex("AAA"), Regex("ZZZ"))
    }

    fun part2(directions: Sequence<Char>, nodes: Map<String, Node>): Long {
        return walk(directions, nodes, Regex("..A"), Regex("..Z"))
    }

    val testInput = parseInput("Day08.example")
    check(part1(testInput.first, testInput.second) == 2L)
    check(part2(testInput.first, testInput.second) == 2L)

    val input = parseInput("Day08.input")
    part1(input.first, input.second).println()
    part2(input.first, input.second).println()
}
