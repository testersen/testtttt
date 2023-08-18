# Configuration: Basic

This configuration is used when you want to automatically configure logback without any frameworks.

> **Warning**
>
> This configuration is known to be incompatible with Spring Boot. Use
> [Configuration: Boot3](https://github.com/telenornorway/autoconf-logback/blob/main/configuration/boot3/README.md) to
> autoconfigure logging for Spring Boot 3.

## Usage

Remember to [configure your environment](https://ghpkg.no/gradle) in order to use packages from GitHub.

```kt
// build.gradle.kts
dependencies {
	runtimeOnly("no.telenor.autoconf.logback:basic")
}
```
