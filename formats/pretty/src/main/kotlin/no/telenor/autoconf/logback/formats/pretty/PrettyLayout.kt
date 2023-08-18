package no.telenor.autoconf.logback.formats.pretty

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.LayoutBase
import no.telenor.autoconf.logback.formats.pretty.formatting.*
import java.time.ZoneId

/** @author James Bradlee <james.bradlee@telenor.no> (https://github.com/j4m350n) */
internal class PrettyLayout(private val configuration: Configuration) : LayoutBase<ILoggingEvent>() {
	override fun doLayout(event: ILoggingEvent): String {
		try {
			return format(event, configuration)
		} catch (ex: Throwable) {
			ex.printStackTrace()
			throw ex
		}
	}
}

internal const val SECTION_PREFIX = "<section> "
internal const val NEWLINE = "\r\n"
internal val NEWLINE_REGEX = Regex("\\r?\\n")
internal val NEWLINE_START = Regex("^[\\r\\n]*")
internal val NEWLINE_END = Regex("[\\r\\n]*$")

internal fun format(
	event: ILoggingEvent,
	configuration: Configuration
): String {
	val levelStyled = configuration.levelStyledFor(event.level.levelInt)
	var str = logHeader(configuration, levelStyled, event)
	var message = event.formattedMessage
	if (configuration.trimMessageLines) message = message.replace(NEWLINE_START, "").replace(NEWLINE_END, "")
	if (configuration.oneEmptyLine) message = trimEmptyLines(message)
	if (configuration.messageStartAsSection) message = SECTION_PREFIX + message

	val showTimestampInMdc = configuration.showTimestamps && configuration.timestampLocation == TimestampLocation.Mdc &&
		(configuration.showMdc || configuration.showTimestampInMdcWhenMdcIsHidden)
	val mdcIsShown = configuration.showMdc && event.mdcPropertyMap.isNotEmpty()
	val messageInHeader = configuration.tryMessageInLogHeader && !showTimestampInMdc && !mdcIsShown
	val renderMdc = showTimestampInMdc || (configuration.showMdc && event.mdcPropertyMap.isNotEmpty())

	if (messageInHeader) str += message

	if (renderMdc) str += mdc(configuration, event, showTimestampInMdc)

	if (!messageInHeader) {
		str += NEWLINE
		if (configuration.messageNotMarkedAsSessionOnFirstLine && !renderMdc) {
			message = message.removePrefix(SECTION_PREFIX)
		}
		str += message
	}

	if (event.throwableProxy != null) {
		if (configuration.emptyLineBeforeThrowable) {
			str += NEWLINE
		}
		str += NEWLINE + formatStackTrace(configuration, event.throwableProxy)
	}

	if (configuration.showTimestamps && configuration.timestampLocation == TimestampLocation.End) {
		if (configuration.emptyLineBeforeTimestampAtEnd) str += NEWLINE
		str += NEWLINE
		str += configuration.style(
			configuration.timestampFormat.format(event.instant.atZone(ZoneId.systemDefault())),
			configuration.timestampStyles
		)
	}

	return (if (configuration.newLineBefore) NEWLINE else "") +
		(if (configuration.showOutline) createOutline(configuration, levelStyled, str) else stripSections(str)) +
		NEWLINE
}
