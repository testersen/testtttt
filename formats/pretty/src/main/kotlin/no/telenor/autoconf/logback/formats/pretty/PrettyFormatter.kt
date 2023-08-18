package no.telenor.autoconf.logback.formats.pretty

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import no.telenor.autoconf.logback.common.Formatter
import java.nio.charset.Charset

object PrettyFormatter : Formatter {
	override fun install(loggerContext: LoggerContext, consoleAppender: ConsoleAppender<ILoggingEvent>) {
		// Pattern
		val pattern = PrettyLayout(Configuration.fromEnvironment())
		pattern.context = loggerContext
		pattern.start()

		// Encoder
		val encoder = LayoutWrappingEncoder<ILoggingEvent>()
		encoder.context = loggerContext
		encoder.charset = Charset.forName("utf-8")
		encoder.layout = pattern

		// Add to appender
		consoleAppender.encoder = encoder
	}
}
