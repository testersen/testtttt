package no.telenor.autoconf.logback.formats.pretty

import ch.qos.logback.classic.Level
import internal.env.Env
import internal.env.buildFromEnvironment
import internal.env.parsers.RegexArray
import internal.utils.colors.*
import java.time.format.DateTimeFormatter
import kotlin.reflect.KParameter

@Suppress("MemberVisibilityCanBePrivate")
@Env("TN_LOG_FORMAT_PRETTY_")
data class Configuration(

	// Flags

	/** Show colors. */
	@Env("ENABLE_COLORS") override val enableColors: Boolean = true,

	/** Print a new line before the log header. */
	@Env("NEWLINE_BEFORE") val newLineBefore: Boolean = true,

	/** Show timestamps. */
	@Env("SHOW_TIMESTAMPS") val showTimestamps: Boolean = true,

	/** Show logger name. */
	@Env("SHOW_LOGGER_NAME") val showLoggerName: Boolean = true,

	/** Show caller information. */
	@Env("SHOW_CALLER_FILE") val showCallerFile: Boolean = true,

	/** Show the file line number if available. */
	@Env("SHOW_CALLER_FILE_LINE") val showCallerFileLine: Boolean = true,

	/** If available, show logging module. */
	@Env("SHOW_CALLER_MODULE") val showCallerModule: Boolean = false,

	/** If available, show version of the logging module. */
	@Env("SHOW_CALLER_MODULE_VERSION") val showCallerModuleVersion: Boolean = true,

	/** Show outline */
	@Env("SHOW_OUTLINE") val showOutline: Boolean = true,

	/**
	 * Hide the log level when the outline is colored with
	 * the log level color.
	 */
	@Env("HIDE_LOG_LEVEL_WHEN_OUTLINE_IS_COLORED_WITH_LOG_LEVEL_COLOR")
	val hideLogLevelWhenOutlineIsColoredWithLogLevelColor: Boolean = false,

	/**
	 * When log level is not visible for user, show a
	 * concealed log level at the end of the log header to
	 * make the log entry indexable (only works if terminal
	 * emulator implements this).
	 *
	 * note: this feature is not supported by intellij idea
	 */
	@Env("SHOW_CONCEALED_LOG_LEVEL_WHEN_LEVEL_NOT_SHOWN")
	val showConcealedLogLevelWhenNotShown: Boolean = true,

	/**
	 * Use the log level color on the outline when colors
	 * are enabled. If colors are enabled, but this flag is
	 * not, the outline should use a fallback color.
	 */
	@Env("SHOW_OUTLINE_IN_LOG_LEVEL_COLOR_IF_COLORS_ARE_ENABLED")
	val showOutlineInLogLevelColorIfColorsAreEnabled: Boolean = true,

	/** Whether to show outline sections. */
	@Env("SHOW_OUTLINE_SECTIONS")
	val showOutlineSections: Boolean = true,

	/**
	 * Whether to put a space between the outline and the log
	 * content.
	 */
	@Env("SHOW_SPACE_AFTER_OUTLINE")
	val showSpaceAfterOutline: Boolean = true,

	/** Show the Mapped Diagnostic Context. */
	@Env("SHOW_MDC")
	val showMdc: Boolean = true,

	/**
	 * If the timestamp location is `Mdc`, show the timestamp
	 * anyway.
	 */
	@Env("SHOW_TIMESTAMP_IN_MDC_WHEN_MDC_IS_HIDDEN")
	val showTimestampInMdcWhenMdcIsHidden: Boolean = false,

	@Env("TRIM_MESSAGE_LINES")
	val trimMessageLines: Boolean = true,

	/**
	 * Attempt to put the message at the end of the log
	 * header. If there ar no visible MDC keys/values and
	 * the timestamp location is not set to Mdc, show the
	 * formattedMessage on the same line as the log header.
	 */
	@Env("TRY_MESSAGE_IN_LOG_HEADER")
	val tryMessageInLogHeader: Boolean = false,


	/**
	 * Pad the end of the log level so that anything that
	 * comes after the log level is always aligned regardless
	 * of which log level is used.
	 */
	@Env("PAD_LOG_LEVEL_END") val padLogLevelEnd: Boolean = true,

	/**
	 * Only one consecutive empty line.
	 *
	 * Note: You can still have empty lines by adding a space
	 * after the newline.
	 */
	@Env("ONLY_ONE_EMPTY_LINE") val oneEmptyLine: Boolean = true,

	/**
	 * When the timestamp location is set to 'End', put an
	 * empty line between the message / stacktrace and the
	 * timestamp.
	 */
	@Env("EMPTY_LINE_BEFORE_TIMESTAMP_AT_END") val emptyLineBeforeTimestampAtEnd: Boolean = false,

	/**
	 * The message start should be marked as a section in the
	 * outline.
	 */
	@Env("MESSAGE_START_AS_SECTION") val messageStartAsSection: Boolean = true,

	/**
	 * If the message is the first line after the log header,
	 * do not show message as section.
	 */
	@Env("MESSAGE_NOT_MARKED_AS_SECTION_ON_FIRST_LINE") val messageNotMarkedAsSessionOnFirstLine: Boolean = true,

	/**
	 * Indicate the start of a stacktrace with a section
	 * symbol in the outline.
	 */
	@Env("STACKTRACE_START_AS_SECTION") val stackTraceStartAsSection: Boolean = true,

	/**
	 * Indicate causedBy of a stacktrace with a section
	 * symbol in the outline.
	 */
	@Env("STACKTRACE_CAUSED_BY_AS_SECTION") val stackTraceCausedByAsSection: Boolean = true,

	/** Show an empty line between the MDC and the message. */
	@Env("EMPTY_LINE_BETWEEN_MDC_AND_MESSAGE") val emptyLineBetweenMdcAndMessage: Boolean = true,

	@Env("MDC_SHOW_ENTRIES_WITH_NULL_VALUE") val mdcShowEntriesWithNullValue: Boolean = true,
	@Env("MDC_SHOW_ENTRIES_WITH_EMPTY_VALUE") val mdcShowEntriesWithEmptyValue: Boolean = true,
	@Env("MDC_TRIM_VALUES") val mdcTrimValues: Boolean = true,

	@Env("EMPTY_LINE_BEFORE_THROWABLE") val emptyLineBeforeThrowable: Boolean = true,

	@Env("SHOW_EXCEPTION_PACKAGE_NAME") val showExceptionPackageName: Boolean = false,
	@Env("SHOW_OMITTED_STACK_TRACE_FRAMES") val showOmittedStackTraceFrames: Boolean = true,
	@Env("SHOW_EXCEPTION_FRAME_PACKAGE_NAME") val showExceptionFramePackageName: Boolean = true,
	@Env("SHOW_EXCEPTION_FRAME_FILE") val showExceptionFrameFile: Boolean = true,
	@Env("SHOW_EXCEPTION_FRAME_FILE_NUMBER") val showExceptionFrameFileNumber: Boolean = true,
	@Env("SHOW_EXCEPTION_FRAME_MODULE") val showExceptionFrameModule: Boolean = true,
	@Env("SHOW_EXCEPTION_FRAME_MODULE_VERSION") val showExceptionFrameModuleVersion: Boolean = true,

	// Timestamp
	@Env("TIMESTAMP_LOCATION")
	val timestampLocation: TimestampLocation = TimestampLocation.Header,
	@Env("TIMESTAMP_FORMAT")
	val timestampFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"),

	// Colors

	@Env("STYLE_LEVEL_ERROR") val errorLevelStyles: Styles = Styles(fg = rgb24(0xFE4A49U)),
	@Env("STYLE_LEVEL_WARN") val warnLevelStyles: Styles = Styles(fg = rgb24(0xFDB833U)),
	@Env("STYLE_LEVEL_INFO") val infoLevelStyles: Styles = Styles(fg = rgb24(0x1789FCU)),
	@Env("STYLE_LEVEL_DEBUG") val debugLevelStyles: Styles = Styles(fg = rgb24(0xBB8D63U)),
	@Env("STYLE_LEVEL_TRACE") val traceLevelStyles: Styles = Styles(fg = rgb24(0x626868U)),
	@Env("STYLE_LEVEL_FALLBACK") val fallbackLevelStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_LEVEL") val individualLevelStyles: Styles = Styles(BOLD),
	@Env("STYLE_LOG_MESSAGE") val logMessage: Styles = Styles(),
	@Env("STYLE_LOG_LEVEL_TO_OTHER_SEPARATOR") val logLevelToOtherStyles: Styles = Styles(),
	@Env("STYLE_LOG_NAME") val logNameStyles: Styles = Styles(BOLD, fg = BRIGHT_GREEN),
	@Env("STYLE_LOG_NAME_TO_OTHER_SEPARATOR") val logNameToOtherStyles: Styles = Styles(),

	/** The style for timestamps, except when timestampLocation is Mdc */
	@Env("STYLE_TIMESTAMPS") val timestampStyles: Styles = Styles(fg = rgb24(0xB48EAEU)),
	@Env("STYLE_TIMESTAMP_TO_OTHER_SEPARATOR") val timestampToOtherSeparatorStyles: Styles = Styles(),
	@Env("STYLE_OPEN_LOGGER_NAME") val openLoggerNameStyles: Styles = Styles(),
	@Env("STYLE_CLOSE_LOGGER_NAME") val closeLoggerNameStyles: Styles = Styles(),
	@Env("STYLE_OPEN_CALLER_FILE") val openCallerFileStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_CLOSE_CALLER_FILE") val closeCallerFileStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_CALLER_FILE_FILENAME") val callerFileNameStyles: Styles = Styles(BOLD, fg = BRIGHT_BLACK),
	@Env("STYLE_CALLER_FILE_FILENAME_TO_LINE") val callerFileNameToLineSeparatorStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_CALLER_FILE_TO_OTHER_SEPARATOR") val callerFileToOtherSeparatorStyles: Styles = Styles(),
	@Env("STYLE_CALLER_FILE_LINE") val callerFileLineStyles: Styles = Styles(BOLD, fg = BRIGHT_BLACK),
	@Env("STYLE_OPEN_CALLER_MOD") val openCallerModStyles: Styles = Styles(),
	@Env("STYLE_CLOSE_CALLER_MOD") val closeCallerModStyles: Styles = Styles(),
	@Env("STYLE_CALLER_MOD_TO_OTHER_SEPARATOR") val callerModToOtherSeparatorStyles: Styles = Styles(),
	@Env("STYLE_CALLER_MOD_NAME_TO_VERSION_SEPARATOR") val callerModNameToVersionSeparatorStyles: Styles = Styles(),
	@Env("STYLE_CALLER_MOD_NAME") val callerModNameStyles: Styles = Styles(),
	@Env("STYLE_CALLER_MOD_VERSION") val callerModVersionStyles: Styles = Styles(),
	@Env("STYLE_MDC_KEY") val mdcKeyStyles: Styles = Styles(fg = rgb24(0xE95678U)),
	@Env("STYLE_MDC_VALUE") val mdcValueStyles: Styles = Styles(fg = rgb24(0xFAB795U)),
	@Env("STYLE_MDC_KEY_VALUE_SEPARATOR") val mdcKeyValueSeparatorStyles: Styles = Styles(),
	@Env("STYLE_MDC_ENTRY_SEPARATOR") val mdcEntrySeparatorStyles: Styles = Styles(),
	@Env("STYLE_MDC_VALUE_NULL") val mdcValueNullStyles: Styles = Styles(fg = rgb24(0xB877DBU)),
	@Env("STYLE_MDC_VALUE_EMPTY") val mdcValueEmptyStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_MDC_KEY_OPEN") val mdcKeyOpenStyles: Styles = mdcKeyStyles,
	@Env("STYLE_MDC_KEY_CLOSE") val mdcKeyCloseStyles: Styles = mdcKeyStyles,
	@Env("STYLE_MDC_VALUE_OPEN") val mdcValueOpenStyles: Styles = mdcValueStyles,
	@Env("STYLE_MDC_VALUE_CLOSE") val mdcValueCloseStyles: Styles = mdcValueStyles,
	@Env("STYLE_EX_CAUSEDBY") val exCausedByStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_CAUSEDBY_TO_ERROR_NAME_SEPARATOR") val exCausedByToErrorNameSeparatorStyles: Styles =
		Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_ERROR_NAME_TO_MESSAGE_SEPARATOR") val exErrorNameToMessageSeparatorStyles: Styles =
		Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_STACK_AT") val exStackAtStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_NAME_PACKAGE") val exNamePackageStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_EX_NAME_PACKAGE_SEPARATOR") val exNamePackageSeparatorStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_EX_NAME_CLASS") val exNameClassStyles: Styles = Styles(BOLD, fg = BRIGHT_RED),
	@Env("STYLE_EX_MESSAGE") val exMessageStyles: Styles = Styles(ITALIC),
	@Env("STYLE_EX_COMMON_FRAMES_OMITTED_TEXT") val exCommonFramesOmittedTextStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_COMMON_FRAMES_OMITTED_NUMBER") val exCommonFramesOmittedNumberStyles: Styles = Styles(BOLD),
	@Env("STYLE_EX_FRAME_NAME_PACKAGE") val exFrameNamePackageStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_EX_FRAME_NAME_PACKAGE_SEPARATOR") val exFrameNamePackageSeparatorStyles: Styles = Styles(fg = BRIGHT_BLACK),
	@Env("STYLE_EX_FRAME_NAME_CLASS") val exFrameNameClassStyles: Styles = Styles(fg = rgb24(0xFAC29AU)),
	@Env("STYLE_EX_FRAME_NAME_METHOD") val exFrameNameMethodStyles: Styles = Styles(fg = rgb24(0x25B0BCU)),
	@Env("STYLE_EX_FRAME_FILE_OPEN") val exFrameFileOpenStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_FRAME_FILE_CLOSE") val exFrameFileCloseStyles: Styles = Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_FRAME_FILE_NAME") val exFrameFileNameStyles: Styles = Styles(BOLD, fg = rgb24(0x646464U)),
	@Env("STYLE_EX_FRAME_FILE_TO_NUMBER_SEPARATOR") var exFrameFileToNumberSeparatorStyles: Styles =
		Styles(fg = rgb24(0x646464U)),
	@Env("STYLE_EX_FRAME_FILE_NUMBER") val exFrameFileNumberStyles: Styles = Styles(BOLD, fg = rgb24(0x646464U)),
	@Env("STYLE_EX_FRAME_MODULE_OPEN") val exFrameModuleOpenStyles: Styles = Styles(fg = GREEN),
	@Env("STYLE_EX_FRAME_MODULE_CLOSE") val exFrameModuleCloseStyles: Styles = Styles(fg = GREEN),
	@Env("STYLE_EX_FRAME_MODULE_TO_VERSION_SEPARATOR") val exFrameModuleToVersionSeparatorStyles: Styles = Styles(fg = GREEN),
	@Env("STYLE_EX_FRAME_MODULE_NAME") val exFrameModuleNameStyles: Styles = Styles(fg = GREEN),
	@Env("STYLE_EX_FRAME_MODULE_VERSION") val exFrameModuleVersionStyles: Styles = Styles(fg = GREEN),

	// Outline Characters

	@Env("OUTLINE_PREFIX_SINGLE") val outlineSingleLine: String = "\u25cf\u0020",
	@Env("OUTLINE_PREFIX_TOP") val outlineTopLine: String = "\u256d\u2500",
	@Env("OUTLINE_PREFIX_BOTTOM") val outlineBottomLine: String = "\u2570\u2500",
	@Env("OUTLINE_PREFIX_MIDDLE") val outlineMiddleLine: String = "\u2502\u0020",
	@Env("OUTLINE_PREFIX_SECTION") val outlineMiddleSectionLine: String = "\u251c\u2500",

	// Other Characters
	@Env("T_LOG_LEVEL_TO_OTHER_SEPARATOR") val tLogLevelToOtherSeparator: String = " ",
	@Env("T_LOG_NAME_TO_OTHER_SEPARATOR") val tLogNameToOtherSeparator: String = " ",
	@Env("T_LOG_HEADER_TIMESTAMP_SEPARATOR") val tLogHeaderTimestampSeparator: String = " ",
	@Env("T_CALLER_MOD_TO_OTHER_SEPARATOR") val tCallerModToOtherSeparator: String = " ",
	@Env("T_OPEN_LOGGER_NAME") val tOpenLoggerName: String = "",
	@Env("T_CLOSE_LOGGER_NAME") val tCloseLoggerName: String = "",
	@Env("T_OPEN_CALLER_FILE") val tOpenCallerFile: String = "(",
	@Env("T_CALLER_FILE_NAME_TO_LINE") val tCallerFileNameToLineSeparator: String = ":",
	@Env("T_CALLER_TO_OTHER_SEPARATOR") val tCallerFileToOtherSeparator: String = " ",
	@Env("T_CLOSE_CALLER_FILE") val tCloseCallerFile: String = ")",
	@Env("T_OPEN_CALLER_MOD") val tOpenCallerMod: String = "",
	@Env("T_CLOSE_CALLER_MOD") val tCloseCallerMod: String = "",
	@Env("T_CALLER_MOD_NAME_TO_VERSION_SEPARATOR") val tCallerModNameToVersionSeparator: String = "@",
	@Env("T_MDC_KEY_VALUE_SEPARATOR") val tMdcKeyValueSeparator: String = " = ",
	@Env("T_MDC_ENTRY_SEPARATOR") val tMdcEntrySeparator: String = NEWLINE,
	@Env("T_MDC_KEY_OPEN") val tMdcKeyOpen: String = "",
	@Env("T_MDC_KEY_CLOSE") val tMdcKeyClose: String = "",
	@Env("T_MDC_VALUE_OPEN") val tMdcValueOpen: String = "\"",
	@Env("T_MDC_VALUE_CLOSE") val tMdcValueClose: String = "\"",
	@Env("T_MDC_VALUE_NULL") val tMdcValueNull: String = "null",
	@Env("T_MDC_VALUE_EMPTY") val tMdcValueEmpty: String = "<empty>",
	@Env("K_MDC_TIMESTAMP_KEY") val kMdcTimestampKey: String = "@timestamp",
	@Env("T_EX_CAUSEDBY") val tExCausedBy: String = "Caused by",
	@Env("T_EX_CAUSEDBY_TO_ERROR_NAME_SEPARATOR") val tExCausedByToErrorNameSeparator: String = ": ",
	@Env("T_EX_NAME_SEPARATOR") val tExNameSeparator: String = ".",
	@Env("T_EX_STACK_AT") val tExStackAt: String = "  at ",
	@Env("T_EX_NAME_TO_MESSAGE_SEPARATOR") val tExNameToMessageSeparator: String = ": ",
	@Env("T_EX_FRAME_METHOD_TO_FILE_SEPARATOR") val tExFrameMethodToFileSeparator: String = "",
	@Env("T_EX_FRAME_FILE_OPEN") val tExFrameFileOpen: String = "(",
	@Env("T_EX_FRAME_FILE_CLOSE") val tExFrameFileClose: String = ")",
	@Env("T_EX_FRAME_FILE_TO_NUMBER_SEPARATOR") val tExFrameFileToNumberSeparator: String = ":",
	@Env("T_EX_FRAME_FILE_TO_MODULE_SEPARATOR") val tExFrameFileToModuleSeparator: String = " ",
	@Env("T_EX_FRAME_MODULE_OPEN") val tExFrameModuleOpen: String = "~",
	@Env("T_EX_FRAME_MODULE_CLOSE") val tExFrameModuleClose: String = "",
	@Env("T_EX_FRAME_MODULE_TO_VERSION_SEPARATOR") val tExFrameModuleToVersionSeparator: String = "@",

	@Env("REGEX_OMIT_STACK_FRAMES") val regexOmitStackFrames: RegexArray = RegexArray(arrayOf()),


	) : WithStyler {
	companion object {
		fun fromEnvironment() = buildFromEnvironment(Configuration::class, ::installStyleParser)
	}

	private val _outlineValues = mapOf(
		"outlineSingleLine" to outlineSingleLine,
		"outlineTopLine" to outlineTopLine,
		"outlineBottomLine" to outlineBottomLine,
		"outlineMiddleLine" to outlineMiddleLine,
		"outlineMiddleSectionLine" to outlineMiddleSectionLine,
	)

	private val _levelStyled = arrayOf(
		_outlineValues
	)

	val fallbackLevelStyled = LevelStyled.with(enableColors, fallbackLevelStyles, *_levelStyled)
	val traceLevelStyled = LevelStyled.with(enableColors, traceLevelStyles, mapOf("levelName" to "TRACE"), *_levelStyled)
	val debugLevelStyled = LevelStyled.with(enableColors, debugLevelStyles, mapOf("levelName" to "DEBUG"), *_levelStyled)
	val infoLevelStyled = LevelStyled.with(enableColors, infoLevelStyles, mapOf("levelName" to "INFO"), *_levelStyled)
	val warnLevelStyled = LevelStyled.with(enableColors, warnLevelStyles, mapOf("levelName" to "WARN"), *_levelStyled)
	val errorLevelStyled = LevelStyled.with(enableColors, errorLevelStyles, mapOf("levelName" to "ERROR"), *_levelStyled)

	fun levelStyledFor(level: Int) = when (level) {
		Level.TRACE_INT -> traceLevelStyled
		Level.DEBUG_INT -> debugLevelStyled
		Level.INFO_INT -> infoLevelStyled
		Level.WARN_INT -> warnLevelStyled
		Level.ERROR_INT -> errorLevelStyled
		else -> fallbackLevelStyled
	}

	val sLogLevelToOtherSeparator = style(tLogLevelToOtherSeparator, logLevelToOtherStyles)
	val sLogNameToOtherSeparator = style(tLogNameToOtherSeparator, logNameToOtherStyles)
	val sLogHeaderTimestampSeparator = style(tLogHeaderTimestampSeparator, timestampToOtherSeparatorStyles)
	val sOpenLoggerName = style(tOpenLoggerName, openLoggerNameStyles)
	val sCloseLoggerName = style(tCloseLoggerName, closeLoggerNameStyles)
	val sOpenCallerFile = style(tOpenCallerFile, openCallerFileStyles)
	val sCloseCallerFile = style(tCloseCallerFile, closeCallerFileStyles)
	val sCallerFileToOtherSeparator = style(tCallerFileToOtherSeparator, callerFileToOtherSeparatorStyles)
	val sCallerFileNameToLineSeparator = style(tCallerFileNameToLineSeparator, callerFileNameToLineSeparatorStyles)
	val sOpenCallerMod = style(tOpenCallerMod, openCallerModStyles)
	val sCloseCallerMod = style(tCloseCallerMod, closeCallerModStyles)
	val sCallerModToOtherSeparator = style(tCallerModToOtherSeparator, callerModToOtherSeparatorStyles)
	val sCallerModNameToVersionSeparator = style(tCallerModNameToVersionSeparator, callerModNameToVersionSeparatorStyles)
	val sMdcKeyValueSeparator = style(tMdcKeyValueSeparator, mdcKeyValueSeparatorStyles)
	val sMdcEntrySeparator = style(tMdcEntrySeparator, mdcEntrySeparatorStyles)
	val sMdcKeyOpen = style(tMdcKeyOpen, mdcKeyOpenStyles)
	val sMdcKeyClose = style(tMdcKeyClose, mdcKeyCloseStyles)
	val sMdcValueOpen = style(tMdcValueOpen, mdcValueOpenStyles)
	val sMdcValueClose = style(tMdcValueClose, mdcValueCloseStyles)
	val sMdcValueNull = style(tMdcValueNull, mdcValueNullStyles)
	val sMdcValueEmpty = style(tMdcValueEmpty, mdcValueEmptyStyles)
	val sExCausedBy = style(tExCausedBy, exCausedByStyles)
	val sExCausedByToNameSeparator = style(tExCausedByToErrorNameSeparator, exCausedByToErrorNameSeparatorStyles)
	val sExNameSeparator = style(tExNameSeparator, exNamePackageSeparatorStyles)
	val sExFrameNameSeparator = style(tExNameSeparator, exFrameNamePackageSeparatorStyles)
	val sExNameToMessageSeparator = style(tExNameToMessageSeparator, exErrorNameToMessageSeparatorStyles)
	val sExStackAt = style(tExStackAt, exStackAtStyles)
	val sExFrameMethodToFileSeparator = style(tExFrameMethodToFileSeparator)
	val sExFrameFileToModuleSeparator = style(tExFrameFileToModuleSeparator)
	val sExFrameFileOpen = style(tExFrameFileOpen, exFrameFileOpenStyles)
	val sExFrameFileClose = style(tExFrameFileClose, exFrameFileCloseStyles)
	val sExFrameFileToNumberSeparator = style(tExFrameFileToNumberSeparator, exFrameFileToNumberSeparatorStyles)
	val sExFrameModuleOpen = style(tExFrameModuleOpen, exFrameModuleOpenStyles)
	val sExFrameModuleClose = style(tExFrameModuleClose, exFrameModuleCloseStyles)
	val sExFrameModuleToVersionSeparator = style(tExFrameModuleToVersionSeparator, exFrameModuleToVersionSeparatorStyles)
}

class LevelStyled(
	override val enableColors: Boolean,
	override val defaultStyles: Styles,
	levelName: String,
	outlineSingleLine: String,
	outlineTopLine: String,
	outlineBottomLine: String,
	outlineMiddleLine: String,
	outlineMiddleSectionLine: String,
) : WithDefaultStyler {
	companion object {
		fun with(
			enableColors: Boolean,
			defaultStyles: Styles,
			vararg parameters: Map<String, String>,
		): LevelStyled {
			val allParams = mutableMapOf(
				"enableColors" to enableColors,
				"defaultStyles" to defaultStyles,
			)
			parameters.forEach { allParams.putAll(it) }
			val params = mutableMapOf<KParameter, Any>()
			val constructor = LevelStyled::class.constructors.first()
			constructor.parameters.forEach { params[it] = allParams[it.name] ?: "" }
			return constructor.callBy(params)
		}
	}

	val levelName = if (levelName.isNotEmpty()) styler(levelName) else ""
	val outlineSingleLine = styler(outlineSingleLine)
	val outlineTopLine = styler(outlineTopLine)
	val outlineBottomLine = styler(outlineBottomLine)
	val outlineMiddleLine = styler(outlineMiddleLine)
	val outlineMiddleSectionLine = styler(outlineMiddleSectionLine)

}

interface WithStyler {
	val enableColors: Boolean

	fun style(text: String, vararg styles: Styles): String {
		if (!this.enableColors) return text
		var output = text
		for (style in styles) output = style.run(output)
		return output
	}
}

interface WithDefaultStyler : WithStyler {
	val defaultStyles: Styles

	fun styler(text: String) = style(text, defaultStyles)
}
