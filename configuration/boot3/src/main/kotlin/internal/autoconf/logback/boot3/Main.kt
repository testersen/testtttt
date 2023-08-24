package internal.autoconf.logback.boot3

import ch.qos.logback.classic.LoggerContext
import no.telenor.autoconf.logback.configure
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

@Suppress("unused")
class SpringbootHijackLogging : ApplicationContextInitializer<ConfigurableApplicationContext> {

	companion object {
		init {
			System.setProperty("spring.main.banner-mode", "off")
			System.setProperty("spring.main.log-startup-info", "off")
		}
	}

	override fun initialize(applicationContext: ConfigurableApplicationContext) {
		val context = LoggerFactory.getILoggerFactory() as LoggerContext
		configure(context)
		for ((loggerName, level) in defaultLogLevels) {
			context.getLogger(loggerName).level = level
		}
	}
}
