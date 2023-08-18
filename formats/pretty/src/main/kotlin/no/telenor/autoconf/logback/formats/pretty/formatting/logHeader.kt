package no.telenor.autoconf.logback.formats.pretty.formatting

import ch.qos.logback.classic.spi.ILoggingEvent
import internal.utils.colors.hidden
import no.telenor.autoconf.logback.formats.pretty.Configuration
import no.telenor.autoconf.logback.formats.pretty.LevelStyled
import no.telenor.autoconf.logback.formats.pretty.TimestampLocation
import java.time.ZoneId

internal fun logHeader(
	configuration: Configuration,
	levelStyled: LevelStyled,
	event: ILoggingEvent
): String {
	val hideLogLevel =
		configuration.hideLogLevelWhenOutlineIsColoredWithLogLevelColor && levelStyled.levelName.isNotEmpty()
	val includeConcealedLogLevelAtEnd = hideLogLevel && configuration.showConcealedLogLevelWhenNotShown

	var str = ""

	// region component/level/start
	if (!hideLogLevel) {
		str += configuration.style(
			levelStyled.levelName.ifEmpty { levelStyled.styler(event.level.levelStr) },
			configuration.individualLevelStyles
		)
		if (configuration.padLogLevelEnd) {
			str += "".padEnd(5 - event.level.levelStr.length)
		}
		str += configuration.sLogLevelToOtherSeparator
	}
	// endregion

	// region component/timestamp/header
	if (configuration.showTimestamps && configuration.timestampLocation == TimestampLocation.Header) {
		str += configuration.style(
			configuration.timestampFormat.format(event.instant.atZone(ZoneId.systemDefault())),
			configuration.timestampStyles
		)
		str += configuration.sLogHeaderTimestampSeparator
	}
	// endregion

	// region component/name
	if (configuration.showLoggerName) {
		str += configuration.sOpenLoggerName
		str += configuration.style(event.loggerName, configuration.logNameStyles)
		str += configuration.sCloseLoggerName
		str += configuration.sLogNameToOtherSeparator
	}
	// endregion

	// region component/caller/file
	if (
		configuration.showCallerFile &&
		event.callerData.isNotEmpty() &&
		event.callerData[0].fileName != null
	) {
		str += configuration.sOpenCallerFile
		str += configuration.style(event.callerData[0].fileName!!, configuration.callerFileNameStyles)
		if (configuration.showCallerFileLine) {
			str += configuration.sCallerFileNameToLineSeparator
			str += configuration.style(event.callerData[0].lineNumber.toString(), configuration.callerFileLineStyles)
		}
		str += configuration.sCloseCallerFile
		str += configuration.sCallerFileToOtherSeparator
	}
	// endregion

	// region component/caller/module
	if (
		configuration.showCallerModule &&
		event.callerData.isNotEmpty() &&
		event.callerData[0]!!.moduleName != null
	) {
		str += configuration.sOpenCallerMod
		str += configuration.style(event.callerData[0]!!.moduleName, configuration.callerModNameStyles)
		if (configuration.showCallerModuleVersion && event.callerData[0]!!.moduleVersion != null) {
			str += configuration.sCallerModNameToVersionSeparator
			str += configuration.style(event.callerData[0]!!.moduleVersion, configuration.callerModVersionStyles)
		}
		str += configuration.sCloseCallerMod
		str += configuration.sCallerModToOtherSeparator
	}
	// endregion

	// region component/level/end
	// note: this feature is not supported by intellij idea
	if (includeConcealedLogLevelAtEnd) str += hidden(event.level.levelStr)
	// endregion

	return str
}
