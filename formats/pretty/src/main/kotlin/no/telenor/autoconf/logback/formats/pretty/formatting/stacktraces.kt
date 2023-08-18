@file:Suppress("DuplicatedCode")

package no.telenor.autoconf.logback.formats.pretty.formatting

import ch.qos.logback.classic.spi.IThrowableProxy
import no.telenor.autoconf.logback.formats.pretty.Configuration
import no.telenor.autoconf.logback.formats.pretty.NEWLINE
import no.telenor.autoconf.logback.formats.pretty.SECTION_PREFIX

private data class ThrowableDefinition(
	val className: String,
	val message: String?,
	val stack: Array<StackTraceElement>
) {
	var cause: ThrowableDefinition? = null

	companion object {
		internal fun fromThrowable(
			ex: Throwable,
			visited: HashMap<Throwable, ThrowableDefinition> = HashMap()
		): ThrowableDefinition {
			if (visited.containsKey(ex)) return visited[ex]!!
			val definition = ThrowableDefinition(
				className = ex::class.java.name,
				message = ex.message,
				stack = ex.stackTrace,
			)
			visited[ex] = definition
			if (ex.cause != null) definition.cause = fromThrowable(ex.cause!!, visited)
			return definition
		}

		internal fun fromThrowable(
			ex: IThrowableProxy,
			visited: HashMap<IThrowableProxy, ThrowableDefinition> = HashMap()
		): ThrowableDefinition {
			if (visited.containsKey(ex)) return visited[ex]!!
			val definition = ThrowableDefinition(
				className = ex.className,
				message = ex.message,
				stack = ex.stackTraceElementProxyArray.map { it.stackTraceElement }.toTypedArray()
			)
			visited[ex] = definition
			if (ex.cause != null) definition.cause = fromThrowable(ex.cause!!, visited)
			return definition
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ThrowableDefinition

		if (className != other.className) return false
		if (message != other.message) return false
		if (!stack.contentEquals(other.stack)) return false
		return cause == other.cause
	}

	override fun hashCode(): Int {
		var result = className.hashCode()
		result = 31 * result + (message?.hashCode() ?: 0)
		result = 31 * result + stack.contentHashCode()
		result = 31 * result + (cause?.hashCode() ?: 0)
		return result
	}
}

private fun formatExceptionName(
	configuration: Configuration,
	name: String
): String {
	val parts = name.split(".").toMutableList()
	val className = configuration.style(parts.removeLast(), configuration.exNameClassStyles)
	val packageName = parts.joinToString(configuration.sExNameSeparator) {
		configuration.style(it, configuration.exNamePackageStyles)
	}
	return (if (configuration.showExceptionPackageName) packageName + (if (packageName.isNotEmpty()) configuration.sExNameSeparator else "") else "") + className
}

private fun formatFrameName(
	configuration: Configuration,
	name: String
): String {
	val parts = name.split(".").toMutableList()
	val className = configuration.style(parts.removeLast(), configuration.exFrameNameClassStyles)
	val packageName = parts.joinToString(configuration.sExFrameNameSeparator) {
		configuration.style(it, configuration.exFrameNamePackageSeparatorStyles)
	}
	return (if (configuration.showExceptionFramePackageName) packageName + (if (packageName.isNotEmpty()) configuration.sExFrameNameSeparator else "") else "") + className
}

private fun formatFrame(
	configuration: Configuration,
	frame: StackTraceElement
): String {
	var str = ""
	str += formatFrameName(configuration, frame.className)
	str += configuration.sExFrameNameSeparator
	str += configuration.style(frame.methodName, configuration.exFrameNameMethodStyles)
	if (configuration.showExceptionFrameFile && frame.fileName != null) {
		str += configuration.sExFrameMethodToFileSeparator + configuration.sExFrameFileOpen
		str += configuration.style(frame.fileName!!, configuration.exFrameFileNameStyles)
		if (configuration.showExceptionFrameFileNumber && frame.lineNumber > 0) {
			str += configuration.sExFrameFileToNumberSeparator
			str += configuration.style(frame.lineNumber.toString(), configuration.exFrameFileNumberStyles)
		}
		str += configuration.sExFrameFileClose
	}
	if (configuration.showExceptionFrameModule && frame.moduleName != null) {
		str += configuration.sExFrameFileToModuleSeparator + configuration.sExFrameModuleOpen
		str += configuration.style(frame.moduleName, configuration.exFrameModuleNameStyles)
		if (configuration.showExceptionFrameModuleVersion && frame.moduleVersion != null) {
			str += configuration.sExFrameModuleToVersionSeparator
			str += configuration.style(frame.moduleVersion, configuration.exFrameModuleVersionStyles)
		}
		str += configuration.sExFrameModuleClose
	}
	return str
}

private fun formatStackTrace(
	configuration: Configuration,
	definition: ThrowableDefinition,
	visited: HashSet<ThrowableDefinition> = HashSet(),
	visitedStes: HashSet<StackTraceElement> = HashSet(),
): String {
	var str = if (configuration.stackTraceStartAsSection) SECTION_PREFIX else ""
	var ex: ThrowableDefinition? = definition

	while (ex != null) {
		str += formatExceptionName(configuration, ex.className)
		if (ex.message != null) {
			str += configuration.sExNameToMessageSeparator
			str += configuration.style(ex.message!!, configuration.exMessageStyles)
		}

		str += ex.stack.filter { frame ->
			!configuration.regexOmitStackFrames.regexes.any {
				var s = "${frame.className}.${frame.methodName}"
				if (frame.fileName != null) {
					s += "(${frame.fileName}"
					if (frame.lineNumber > 0) {
						s += ":${frame.lineNumber}"
					}
					s += ")"
				}
				if (frame.moduleName != null) {
					s += "~${frame.moduleName}"
					if (frame.moduleVersion != null) {
						s += "@${frame.moduleVersion}"
					}
				}
				it.containsMatchIn(s)
			}
		}.joinToString("") {
			if (visitedStes.contains(it)) {
				""
			} else {
				NEWLINE + configuration.sExStackAt + formatFrame(configuration, it)
			}
		}

		if (configuration.showOmittedStackTraceFrames) {
			val n = ex.stack.filter { visitedStes.contains(it) }.size
			if (n > 0) {
				str += NEWLINE + configuration.style(
					"  ... ${
						configuration.style(
							n.toString(),
							configuration.exCommonFramesOmittedNumberStyles
						)
					} common frames omitted",
					configuration.exCommonFramesOmittedTextStyles
				)
			}
		}

		visitedStes.addAll(ex.stack)
		visited.add(ex)
		ex = ex.cause
		if (visited.contains(ex)) break
		if (ex != null) {
			str += NEWLINE
			if (configuration.stackTraceCausedByAsSection) {
				str += SECTION_PREFIX
			}
			str += configuration.sExCausedBy + configuration.sExCausedByToNameSeparator
		}
	}

	return str
}

internal fun formatStackTrace(configuration: Configuration, ex: Throwable) =
	formatStackTrace(configuration, ThrowableDefinition.fromThrowable(ex))

internal fun formatStackTrace(configuration: Configuration, ex: IThrowableProxy) =
	formatStackTrace(configuration, ThrowableDefinition.fromThrowable(ex))
