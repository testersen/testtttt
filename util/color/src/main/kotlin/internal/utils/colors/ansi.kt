package internal.utils.colors

import kotlin.reflect.KClass

data class Code(val open: String, val close: String, val regexp: Regex)
data class Rgb(val r: UByte, val g: UByte, val b: UByte)

internal var enabled = true

fun setColorEnabled(value: Boolean) {
	enabled = value
}

fun getColorEnabled() = enabled

/**
 * Builds graphic internal.utils.colors.code.
 * @param open Opening graphic internal.utils.colors.code.
 * @param close Closing graphic internal.utils.colors.code.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun code(close: UByte, vararg open: UByte): Code {
	return Code(
		"\u001b[${open.joinToString(";")}m",
		"\u001b[${close}m",
		Regex("\\u001b\\[${close}m")
	)
}

/**
 * Applies color and background based on color internal.utils.colors.code and its associated text.
 * @param str Text to graphics color settings to.
 * @param code Graphic internal.utils.colors.code to apply.
 */
fun runCode(str: String, code: Code): String {
	return if (enabled) "${code.open}${str.replace(code.regexp, code.close)}${code.close}" else str
}

// @formatter:off
@OptIn(ExperimentalUnsignedTypes::class) val RESET = code(0U, 0U)
@OptIn(ExperimentalUnsignedTypes::class) val BOLD = code(22U, 1U)
@OptIn(ExperimentalUnsignedTypes::class) val DIM = code(22U, 1U)
@OptIn(ExperimentalUnsignedTypes::class) val ITALIC = code(23U, 3U)
@OptIn(ExperimentalUnsignedTypes::class) val UNDERLINE = code(24U, 4U)
@OptIn(ExperimentalUnsignedTypes::class) val INVERSE = code(27U, 7U)
@OptIn(ExperimentalUnsignedTypes::class) val HIDDEN = code(28U, 8U)
@OptIn(ExperimentalUnsignedTypes::class) val STRIKETHROUGH = code(29U, 9U)
@OptIn(ExperimentalUnsignedTypes::class) val BLACK = code(39U, 30U)
@OptIn(ExperimentalUnsignedTypes::class) val RED = code(39U, 31U)
@OptIn(ExperimentalUnsignedTypes::class) val GREEN = code(39U, 32U)
@OptIn(ExperimentalUnsignedTypes::class) val YELLOW = code(39U, 33U)
@OptIn(ExperimentalUnsignedTypes::class) val BLUE = code(39U, 34U)
@OptIn(ExperimentalUnsignedTypes::class) val MAGENTA = code(39U, 35U)
@OptIn(ExperimentalUnsignedTypes::class) val CYAN = code(39U, 36U)
@OptIn(ExperimentalUnsignedTypes::class) val WHITE = code(39U, 37U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_BLACK = code(39U, 90U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_RED = code(39U, 91U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_GREEN = code(39U, 92U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_YELLOW = code(39U, 93U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_BLUE = code(39U, 94U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_MAGENTA = code(39U, 95U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_CYAN = code(39U, 96U)
@OptIn(ExperimentalUnsignedTypes::class) val BRIGHT_WHITE = code(39U, 97U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BLACK = code(49U, 40U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_RED = code(49U, 41U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_GREEN = code(49U, 42U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_YELLOW = code(49U, 43U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BLUE = code(49U, 44U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_MAGENTA = code(49U, 45U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_CYAN = code(49U, 46U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_WHITE = code(49U, 47U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_BLACK = code(49U, 100U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_RED = code(49U, 101U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_GREEN = code(49U, 102U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_YELLOW = code(49U, 103U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_BLUE = code(49U, 104U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_MAGENTA = code(49U, 105U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_CYAN = code(49U, 106U)
@OptIn(ExperimentalUnsignedTypes::class) val BG_BRIGHT_WHITE = code(49U, 107U)
// @formatter:on

fun reset(text: String) = runCode(text, RESET)
fun bold(text: String) = runCode(text, BOLD)
fun dim(text: String) = runCode(text, DIM)
fun italic(text: String) = runCode(text, ITALIC)
fun underline(text: String) = runCode(text, UNDERLINE)
fun inverse(text: String) = runCode(text, INVERSE)
fun hidden(text: String) = runCode(text, HIDDEN)
fun strikeThrough(text: String) = runCode(text, STRIKETHROUGH)
fun black(text: String) = runCode(text, BLACK)
fun red(text: String) = runCode(text, RED)
fun green(text: String) = runCode(text, GREEN)
fun yellow(text: String) = runCode(text, YELLOW)
fun blue(text: String) = runCode(text, BLUE)
fun magenta(text: String) = runCode(text, MAGENTA)
fun cyan(text: String) = runCode(text, CYAN)
fun white(text: String) = runCode(text, WHITE)
fun brightBlack(text: String) = runCode(text, BRIGHT_BLACK)
fun brightRed(text: String) = runCode(text, BRIGHT_RED)
fun brightGreen(text: String) = runCode(text, BRIGHT_GREEN)
fun brightYellow(text: String) = runCode(text, BRIGHT_YELLOW)
fun brightBlue(text: String) = runCode(text, BRIGHT_BLUE)
fun brightMagenta(text: String) = runCode(text, BRIGHT_MAGENTA)
fun brightCyan(text: String) = runCode(text, BRIGHT_CYAN)
fun brightWhite(text: String) = runCode(text, BRIGHT_WHITE)
fun bgBlack(text: String) = runCode(text, BG_BLACK)
fun bgRed(text: String) = runCode(text, BG_RED)
fun bgGreen(text: String) = runCode(text, BG_GREEN)
fun bgYellow(text: String) = runCode(text, BG_YELLOW)
fun bgBlue(text: String) = runCode(text, BG_BLUE)
fun bgMagenta(text: String) = runCode(text, BG_MAGENTA)
fun bgCyan(text: String) = runCode(text, BG_CYAN)
fun bgWhite(text: String) = runCode(text, BG_WHITE)
fun bgBrightBlack(text: String) = runCode(text, BG_BRIGHT_BLACK)
fun bgBrightRed(text: String) = runCode(text, BG_BRIGHT_RED)
fun bgBrightGreen(text: String) = runCode(text, BG_BRIGHT_GREEN)
fun bgBrightYellow(text: String) = runCode(text, BG_BRIGHT_YELLOW)
fun bgBrightBlue(text: String) = runCode(text, BG_BRIGHT_BLUE)
fun bgBrightMagenta(text: String) = runCode(text, BG_BRIGHT_MAGENTA)
fun bgBrightCyan(text: String) = runCode(text, BG_BRIGHT_CYAN)
fun bgBrightWhite(text: String) = runCode(text, BG_BRIGHT_WHITE)

//  Special color sequences

internal fun clamp(n: UByte, max: UByte = 255U, min: UByte = 0U) = if (n > max) max else if (n < min) min else n

@OptIn(ExperimentalUnsignedTypes::class)
fun rgb8(color: UByte) = code(39U, 38U, 5U, clamp(color))

@OptIn(ExperimentalUnsignedTypes::class)
fun bgRgb8(color: UByte) = code(49U, 48U, 5U, clamp(color))

@OptIn(ExperimentalUnsignedTypes::class)
fun rgb24(r: UByte, g: UByte, b: UByte) = code(39U, 38U, 2U, r, g, b)

fun rgb24(color: UInt) = rgb24(
	((color shr 16) and 0xFFU).toUByte(),
	((color shr 8) and 0xFFU).toUByte(),
	(color and 0xFFU).toUByte()
)

fun rgb24(color: Rgb) = rgb24(color.r, color.g, color.b)

@OptIn(ExperimentalUnsignedTypes::class)
fun bgRgb24(r: UByte, g: UByte, b: UByte) = code(49U, 48U, 2U, r, g, b)

fun bgRgb24(color: UInt) =
	bgRgb24(((color shr 16) and 8U).toUByte(), ((color shr 8) and 8U).toUByte(), (color and 8U).toUByte())

fun bgRgb24(color: Rgb) = bgRgb24(color.r, color.g, color.b)

class Styles(
	private vararg val decorations: Code,
	private val fg: Code? = null,
	private val bg: Code? = null,
) {
	fun run(txt: String): String {
		var output = txt
		if (fg != null) output = runCode(output, fg)
		if (bg != null) output = runCode(output, bg)
		for (code in decorations) {
			output = runCode(output, code)
		}
		return output
	}

	companion object {
		fun parse(options: String): Styles {
			val decorations = mutableListOf<Code>()
			var fg: Code? = null
			var bg: Code? = null

			for (option in options.split(Regex("[,;\\s]+]"))) {
				val eqIndex = option.indexOf("=")
				if (eqIndex == -1) {
					val code = decorationTable[option] ?: continue
					decorations.add(code)
					continue
				}
				val key = option.substring(0, eqIndex)
				val value = option.substring(eqIndex + 1)
				if (key == "fg") {
					val code = fgTable[value]
					if (code != null) {
						fg = code
						continue
					}
					if (value.startsWith("#")) {
						fg = rgb24(value.substring(1).toUInt(16))
					}
				} else if (key == "bg") {
					val code = bgTable[value]
					if (code != null) {
						bg = code
						continue
					}
					if (value.startsWith("#")) {
						bg = bgRgb24(value.substring(1).toUInt(16))
					}
				}
			}
			return Styles(*decorations.toTypedArray(), fg = fg, bg = bg)
		}
	}
}

fun installStyleParser(): Map<KClass<*>, (String) -> Any> {
	return mapOf(Styles::class to { Styles.parse(it) })
}

private val decorationTable = mutableMapOf(
	"reset" to RESET,
	"bold" to BOLD,
	"dim" to DIM,
	"italic" to ITALIC,
	"underline" to UNDERLINE,
	"inverse" to INVERSE,
	"hidden" to HIDDEN,
	"strikethrough" to STRIKETHROUGH,
)

private val fgTable = mutableMapOf(
	"black" to BLACK,
	"red" to RED,
	"green" to GREEN,
	"yellow" to YELLOW,
	"blue" to BLUE,
	"magenta" to MAGENTA,
	"cyan" to CYAN,
	"white" to WHITE,
	"brightBlack" to BRIGHT_BLACK,
	"brightRed" to BRIGHT_RED,
	"brightGreen" to BRIGHT_GREEN,
	"brightYellow" to BRIGHT_YELLOW,
	"brightBlue" to BRIGHT_BLUE,
	"brightMagenta" to BRIGHT_MAGENTA,
	"brightCyan" to BRIGHT_CYAN,
	"brightWhite" to BRIGHT_WHITE,
)

private val bgTable = mutableMapOf(
	"black" to BG_BLACK,
	"red" to BG_RED,
	"green" to BG_GREEN,
	"yellow" to BG_YELLOW,
	"blue" to BG_BLUE,
	"magenta" to BG_MAGENTA,
	"cyan" to BG_CYAN,
	"white" to BG_WHITE,
	"brightBlack" to BG_BRIGHT_BLACK,
	"brightRed" to BG_BRIGHT_RED,
	"brightGreen" to BG_BRIGHT_GREEN,
	"brightYellow" to BG_BRIGHT_YELLOW,
	"brightBlue" to BG_BRIGHT_BLUE,
	"brightMagenta" to BG_BRIGHT_MAGENTA,
	"brightCyan" to BG_BRIGHT_CYAN,
	"brightWhite" to BG_BRIGHT_WHITE,
)
