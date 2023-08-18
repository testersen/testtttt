package no.telenor.autoconf.logback.formats.json

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import net.logstash.logback.composite.loggingevent.*
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder
import no.telenor.autoconf.logback.common.Formatter

/** @author James Bradlee <james.bradlee@telenor.no> (https://github.com/j4m350n) */
object JsonFormatter : Formatter {
	override fun install(
		loggerContext: LoggerContext,
		consoleAppender: ConsoleAppender<ILoggingEvent>
	) {
		val pattern = LoggingEventPatternJsonProvider()
		pattern.context = loggerContext
		pattern.pattern =
			"""
				{
				"message": "#tryJson{%message}",
				"level": "%level",
				"logClass": "%class",
				"logMethod": "%method"
				}
			"""

		val encoder = LoggingEventCompositeJsonEncoder()
		encoder.providers.addProvider(LoggingEventFormattedTimestampJsonProvider().apply { fieldName = "timestamp" })
		encoder.providers.addProvider(MdcJsonProvider())
		encoder.providers.addProvider(StackTraceJsonProvider())
		encoder.providers.addProvider(LogstashMarkersJsonProvider())
		encoder.providers.addProvider(pattern)
		encoder.context = loggerContext
		encoder.start()

		consoleAppender.encoder = encoder
	}
}
