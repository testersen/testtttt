package internal.autoconf.logback.basic

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.core.spi.ContextAwareBase

@Suppress("unused")
class HijackLogback : ContextAwareBase(), Configurator {
	override fun configure(loggerContext: LoggerContext): Configurator.ExecutionStatus {
		configure(loggerContext)
		return Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
	}
}
