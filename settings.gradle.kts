rootProject.name = "autoconf-logback"

pluginManagement {
	plugins {
		kotlin("jvm") version "1.9.0"
	}
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
	versionCatalogs {
		create("kt") {
			val version = "1.9.0"
			library("reflect", "org.jetbrains.kotlin:kotlin-reflect:$version")
		}

		create("logs") {
			library("logback", "ch.qos.logback:logback-classic:1.4.8")
			library("logstash", "net.logstash.logback:logstash-logback-encoder:7.4")
		}

		create("spring") {
			library("context", "org.springframework:spring-context:6.0.11")
		}
	}
}

arrayOf(
	":common",
	":util:color",
	":util:env",
	":autoconf",
	":formats:json",
	":formats:pretty",
	":configuration:basic",
	":configuration:boot3",
).forEach { include(it) }
