package internal.env.parsers

import kotlin.reflect.KClass

val booleanValues = mutableMapOf(
	// truthy
	"true" to true,
	"y" to true,
	"yes" to true,
	"on" to true,
	"enable" to true,
	"enabled" to true,
	"active" to true,
	"activated" to true,

	// falsy
	"false" to false,
	"n" to false,
	"no" to false,
	"off" to false,
	"disable" to false,
	"disabled" to false,
	"inactive" to false,
)

internal fun installBooleanParser(): Map<KClass<*>, (String) -> Any> = mapOf(
	Boolean::class to {
		booleanValues[it.trim().lowercase()] ?: throw Exception("'${it}' is not a boolean value")
	}
)
