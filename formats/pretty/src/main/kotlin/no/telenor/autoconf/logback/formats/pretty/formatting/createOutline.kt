package no.telenor.autoconf.logback.formats.pretty.formatting

import no.telenor.autoconf.logback.formats.pretty.*

internal fun createOutline(
	configuration: Configuration,
	levelStyled: LevelStyled,
	text: String,
): String {
	val spaceAfterOutline = if (configuration.showSpaceAfterOutline) " " else ""
	val outlineStyled =
		if (configuration.showOutlineInLogLevelColorIfColorsAreEnabled) levelStyled else configuration.fallbackLevelStyled

	val lines = text.split(NEWLINE_REGEX).toMutableList()
	if (lines.size == 1) return "${outlineStyled.outlineSingleLine}${spaceAfterOutline}${lines[0]}"
	return "${outlineStyled.outlineTopLine}${spaceAfterOutline}${lines[0].removePrefix(SECTION_PREFIX)}" +
		lines.slice(1..<lines.size - 1)
			.joinToString("") {
				"$NEWLINE${
					if (it.startsWith(SECTION_PREFIX)) outlineStyled.outlineMiddleSectionLine
					else outlineStyled.outlineMiddleLine
				}${spaceAfterOutline}${it.removePrefix(SECTION_PREFIX)}"
			} +
		"$NEWLINE${outlineStyled.outlineBottomLine}${spaceAfterOutline}${lines[lines.size - 1].removePrefix(SECTION_PREFIX)}"
}
