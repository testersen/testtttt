package internal.env

import internal.env.parsers.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.jvmName

fun parseEnumValue(enum: KClass<*>, value: String): Any = enum.functions
	.first { it.name == "valueOf" }
	.call(value)!!

internal class Parsers(vararg additionalParsers: KFunction<Map<KClass<*>, (String) -> Any>>) {
	private val parsers = HashMap<KClass<*>, (String) -> Any>()

	init {
		parsers.putAll(installStringParser())
		parsers.putAll(installBooleanParser())
		parsers.putAll(installIntParsers())
		parsers.putAll(installDateTimeFormatter())
		parsers.putAll(installRegexArrayParser())

		for (parser in additionalParsers) {
			if (parser.parameters.any { !it.isOptional }) {
				throw Exception("Could not call '${parser.name}', function has non-optional parameters!")
			}
			parsers.putAll(parser.call())
		}
	}

	fun parse(type: KClass<*>, value: String?): Any? {
		value ?: return null

		@Suppress("UNCHECKED_CAST")
		if (type.java.isEnum) return parseEnumValue(type as KClass<Enum<*>>, value)

		val parser = parsers[type] ?: throw Exception("No value parser for type '${type.jvmName}'")
		return parser(value)
	}
}
