package internal.env.parsers

import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

fun installDateTimeFormatter(): Map<KClass<*>, (String) -> Any> = mapOf(
	DateTimeFormatter::class to { DateTimeFormatter.ofPattern(it) }
)
