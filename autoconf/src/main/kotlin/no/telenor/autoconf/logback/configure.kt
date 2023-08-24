package no.telenor.autoconf.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import no.telenor.autoconf.logback.formats.json.JsonFormatter
import no.telenor.autoconf.logback.formats.pretty.PrettyFormatter

fun configure(loggerContext: LoggerContext): Configuration {
	val configuration = try {
		Configuration.fromEnvironment()
	} catch (ex: Throwable) {
		panic("Could not load configurations", ex)
	}

	val formatters = mutableMapOf(
		"json" to JsonFormatter,
		"pretty" to PrettyFormatter
	)

	// todo(James Bradlee @testersen): Search in ~/.config/autoconf-logback/formatters/*.jar for local formatters.

	val format = formatters[configuration.format] ?: panic("Could not find formatter '${configuration.format}'")

	val appender = ConsoleAppender<ILoggingEvent>()
	appender.context = loggerContext
	appender.name = "console"

	try {
		format.install(loggerContext, appender)
	} catch (ex: Throwable) {
		panic("Something went wrong when installing formatter '${configuration.format}'", ex)
	}

	appender.start()

	val mainLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
	mainLogger.level = Level.toLevel(configuration.logLevel.name);
	mainLogger.detachAndStopAllAppenders()
	mainLogger.isAdditive = false
	mainLogger.addAppender(appender)

	return configuration
}
