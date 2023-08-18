package no.telenor.autoconf.logback.formats.pretty.formatting

import no.telenor.autoconf.logback.formats.pretty.SECTION_PREFIX

/**
 * Strip `<section> ` from log messages when outline is disabled.
 */
internal fun stripSections(text: String) = text.replace("\n$SECTION_PREFIX", "\n")
