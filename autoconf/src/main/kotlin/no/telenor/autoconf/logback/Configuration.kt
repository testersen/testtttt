package no.telenor.autoconf.logback

import internal.env.Env
import internal.env.buildFromEnvironment
import org.slf4j.event.Level

private val isBuildEnvironment = !System.getProperty("java.class.path", "").contains(".gradle/caches")

data class Configuration(
	/**
	 * Which configuration to use.
	 *
	 * By default, the format will be `json` for build
	 * environments and `pretty` when run with Gradle.
	 */
	@Env("TN_LOG_FORMAT")
	val format: String = if (isBuildEnvironment) "json" else "pretty",

	/**
	 * The log level to use for all loggers by default.
	 */
	@Env("TN_LOG_LEVEL")
	val logLevel: Level = Level.INFO,
) {
	companion object {
		fun fromEnvironment() = buildFromEnvironment(Configuration::class)
	}
}
