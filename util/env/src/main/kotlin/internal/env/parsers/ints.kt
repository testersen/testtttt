package internal.env.parsers

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

private fun <T : Number> radix(
	handler: (String, Int) -> T
): (String) -> Any = {
	val signed = it.substring(0, 1).let { s -> s == "-" || s == "+" }
	val sign = if (signed) it.substring(0, 1) else ""
	var number = if (signed) it.substring(1) else it
	val radix = when (number.substring(0, 2)) {
		"0b" -> 2
		"0o" -> 8
		"0x" -> 16
		else -> 10
	}
	number = sign + (if (radix == 10) number else number.substring(2))
	handler(number, radix)
}

fun installIntParsers(): Map<KClass<*>, (String) -> Any> = mapOf(
	Float::class to { it.toFloat() },
	Double::class to { it.toDouble() },
	BigDecimal::class to { it.toBigDecimal() },
	UByte::class to { it.toUByte() },
	UShort::class to { it.toUShort() },
	UInt::class to { it.toUInt() },
	ULong::class to { it.toULong() },

	Byte::class to radix { number, radix -> number.toByte(radix) },
	Short::class to radix { number, radix -> number.toShort(radix) },
	Int::class to radix { number, radix -> number.toInt(radix) },
	Long::class to radix { number, radix -> number.toLong(radix) },
	BigInteger::class to radix { number, radix -> number.toBigInteger(radix) },
)
