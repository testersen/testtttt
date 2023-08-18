plugins {
	kotlin("jvm") version "1.9.0"
	jacoco
	`java-library`
	`maven-publish`
}

allprojects {
	group = "no.telenor.autoconf.logback"
	version = System.getProperties().getOrDefault("VERSION", "UNVERSIONED")

	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "jacoco")
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")

	repositories {
		mavenCentral()
	}

	dependencies {
		testImplementation(kotlin("test"))
	}

	tasks.test {
		useJUnitPlatform()
	}

	kotlin {
		jvmToolchain(17)
	}

	publishing {
		publications {
			register<MavenPublication>("gpr") {
				from(components["kotlin"])
			}
		}
	}
}
