package no.telenor.autoconf.logback.formats.pretty.formatting

import no.telenor.autoconf.logback.formats.pretty.NEWLINE
import no.telenor.autoconf.logback.formats.pretty.NEWLINE_REGEX

val MANY_LINES = Regex("(${NEWLINE_REGEX.pattern}){2,}")
val ONE_EMPTY_LINE = NEWLINE.repeat(2)

internal fun trimEmptyLines(message: String) = message.replace(MANY_LINES, ONE_EMPTY_LINE)
