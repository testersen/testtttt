package no.telenor.autoconf.logback

import internal.env.Env
import internal.env.buildFromEnvironment

private val isBuildEnvironment = !System.getProperty("java.class.path", "").contains(".gradle/caches")

internal data class Configuration(
	/**
	 * Which configuration to use.
	 *
	 * By default, the format will be `json` for build
	 * environments and `pretty` when run with Gradle.
	 */
	@Env("TN_LOG_FORMAT")
	val format: String = if (isBuildEnvironment) "json" else "pretty"
) {
	companion object {
		fun fromEnvironment() = buildFromEnvironment(Configuration::class)
	}
}
