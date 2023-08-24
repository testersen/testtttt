package no.telenor.autoconf.logback.formats.powerline

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import no.telenor.autoconf.logback.common.Formatter

object PowerlineFormatter : Formatter {
	override fun install(
		loggerContext: LoggerContext,
		consoleAppender: ConsoleAppender<ILoggingEvent>
	) {

	}
}
