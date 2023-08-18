package internal.env.parsers

import kotlin.reflect.KClass

class RegexArray(val regexes: Array<Regex>)

internal fun installRegexArrayParser(): Map<KClass<*>, (String) -> Any> = mapOf(
	RegexArray::class to { allStr ->
		RegexArray(allStr.split(";").map { Regex(it) }.toTypedArray())
	}
)
