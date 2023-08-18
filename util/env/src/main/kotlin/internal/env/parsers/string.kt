package internal.env.parsers

import kotlin.reflect.KClass

internal fun installStringParser(): Map<KClass<*>, (String) -> Any> = mapOf(String::class to { it })
