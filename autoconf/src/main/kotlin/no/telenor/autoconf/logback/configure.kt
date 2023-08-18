package no.telenor.autoconf.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import no.telenor.autoconf.logback.common.Formatter
import no.telenor.autoconf.logback.formats.json.JsonFormatter
import no.telenor.autoconf.logback.formats.pretty.PrettyFormatter

internal object Autoconf : Formatter {
	override fun install(
		loggerContext: LoggerContext,
		consoleAppender: ConsoleAppender<ILoggingEvent>
	) {
		val formatters = mutableMapOf(
			"json" to JsonFormatter,
			"pretty" to PrettyFormatter
		)

		val configuration = try {
			Configuration.fromEnvironment()
		} catch (ex: Throwable) {
			panic("Could not load configurations", ex)
		}

		// todo(testersen): Search in ~/.config/autoconf-logback/formatters/*.jar for local formatters.

		val format = formatters[configuration.format] ?: panic("Could not find formatter '${configuration.format}'")

		try {
			format.install(loggerContext, consoleAppender)
		} catch (ex: Throwable) {
			panic("Something went wrong when installing formatter '${configuration.format}'", ex)
		}
	}
}

fun configure(loggerContext: LoggerContext) {
	val appender = ConsoleAppender<ILoggingEvent>()
	appender.context = loggerContext
	appender.name = "console"

	Autoconf.install(loggerContext, appender)

	appender.start()

	val mainLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
	mainLogger.level = Level.INFO
	mainLogger.detachAndStopAllAppenders()
	mainLogger.isAdditive = false
	mainLogger.addAppender(appender)
}
