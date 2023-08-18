package no.telenor.autoconf.logback

import internal.utils.colors.bold
import internal.utils.colors.brightRed
import kotlin.system.exitProcess

internal fun panic(message: String, throwable: Throwable? = null): Nothing {
	throwable?.printStackTrace()
	println("${bold(brightRed("error"))}: $message")
	exitProcess(1)
}
