import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Read chunks/paragraphs from the given input txt file.
 */
fun readInputChunks(name: String) = Path("src/$name.txt").readText().split(Regex("\n\n"))

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

operator fun LongRange.plus(offset: Long): LongRange = (first + offset)..(last + offset)
operator fun LongRange.contains(other: LongRange): Boolean = other.first >= start && other.last <= last

fun LongRange.overlaps(other: LongRange): Boolean = (other.first in first..last || other.last in first..last)

fun LongRange.cut(window: LongRange): List<LongRange> {
    val intersectionLower = max(window.first, first)
    val intersectionUpper = min(window.last, last)
    return if (overlaps(window)) {
        listOf(first..<intersectionLower, intersectionLower..intersectionUpper, (intersectionUpper + 1)..last)
            .filter { it.first <= it.last }
    } else {
        listOf(this)
    }
}

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun Long.gcd(that: Long): Long {
    var a = this
    var b = that
    while (b != 0L) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun Long.lcm(that: Long): Long {
    return (this * that).absoluteValue / this.gcd(that)
}
