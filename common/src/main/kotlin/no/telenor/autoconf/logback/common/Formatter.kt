package no.telenor.autoconf.logback.common

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender

interface Formatter {
	fun install(
		loggerContext: LoggerContext,
		consoleAppender: ConsoleAppender<ILoggingEvent>
	)
}
