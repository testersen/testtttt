package no.telenor.autoconf.logback.formats.pretty.formatting

import ch.qos.logback.classic.spi.ILoggingEvent
import no.telenor.autoconf.logback.formats.pretty.Configuration
import no.telenor.autoconf.logback.formats.pretty.NEWLINE
import java.time.ZoneId

private fun formatValue(configuration: Configuration, value: String?) = if (value == null) {
	configuration.sMdcValueNull
} else if (value.isEmpty()) {
	configuration.sMdcValueEmpty
} else {
	"${configuration.sMdcValueOpen}${
		configuration.style(value, configuration.mdcValueStyles)
	}${configuration.sMdcValueClose}"
}

private fun formatKeyValue(configuration: Configuration, key: String, value: String?) =
	"${configuration.sMdcKeyOpen}${
		configuration.style(key, configuration.mdcKeyStyles)
	}${configuration.sMdcKeyClose}${configuration.sMdcKeyValueSeparator}${formatValue(configuration, value)}"

internal fun mdc(
	configuration: Configuration,
	event: ILoggingEvent,
	showTimestamp: Boolean,
): String {
	val entries = mutableListOf<String>()

	if (showTimestamp) {
		entries.add(
			formatKeyValue(
				configuration,
				configuration.kMdcTimestampKey,
				configuration.timestampFormat.format(event.instant.atZone(ZoneId.systemDefault()))
			)
		)
	}

	entries.addAll(
		event.mdcPropertyMap
			.filter {
				if (it.value == null && !configuration.mdcShowEntriesWithNullValue) false
				else !(it.value != null && (if (configuration.mdcTrimValues) it.value.trim() else it.value).isEmpty() && !configuration.mdcShowEntriesWithEmptyValue)
			}
			.map { formatKeyValue(configuration, it.key, if (configuration.mdcTrimValues) it.value?.trim() else it.value) }
	)

	return (if (entries.isNotEmpty()) NEWLINE else "") +
		entries.joinToString(configuration.sMdcEntrySeparator) +
		(if (entries.isNotEmpty() && configuration.emptyLineBetweenMdcAndMessage) NEWLINE else "")
}
