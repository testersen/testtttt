plugins {
	kotlin("jvm")
	id("no.ghpkg")
	jacoco
	`java-library`
	`maven-publish`
}

allprojects {
	group = "no.telenor.autoconf.logback"
	version = System.getProperties().getOrDefault("VERSION", "0.1.0-SNAPSHOT")

	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "jacoco")
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "no.ghpkg")

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
		repositories {
			github.actions()
		}
		publications {
			register<MavenPublication>("gpr") {
				from(components["kotlin"])
			}
		}
	}
}
