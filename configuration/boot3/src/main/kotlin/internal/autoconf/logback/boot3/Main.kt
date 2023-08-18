package internal.autoconf.logback.boot3

import ch.qos.logback.classic.LoggerContext
import no.telenor.autoconf.logback.configure
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

@Suppress("unused")
class SpringbootHijackLogging : ApplicationContextInitializer<ConfigurableApplicationContext> {
	override fun initialize(applicationContext: ConfigurableApplicationContext) {
		applicationContext.environment.systemProperties["spring.main.banner-mode"] = "off"
		configure(LoggerFactory.getILoggerFactory() as LoggerContext)
	}
}
