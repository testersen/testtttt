# Configuration: Spring Boot 3

This configuration will automatically configure Spring Boot 3, it will also disable the Spring banner.

## Usage

Remember to [configure your environment](https://ghpkg.no/gradle) in order to use packages from GitHub.

```kt
// build.gradle.kts
dependencies {
	runtimeOnly("no.telenor.autoconf.logback:boot3")
}
```
