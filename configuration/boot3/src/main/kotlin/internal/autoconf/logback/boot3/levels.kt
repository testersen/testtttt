package internal.autoconf.logback.boot3

import ch.qos.logback.classic.Level

private val TO_WARN = arrayOf(
	"org.springframework.context.support.PostProcessorRegistrationDelegate\$BeanPostProcessorChecker",
	"org.springframework.ws.soap.addressing.server.AnnotationActionEndpointMapping",
	"org.apache.catalina.core.StandardService",
	"org.apache.catalina.core.StandardEngine",
	"org.apache.catalina.core.ContainerBase",
	"org.springframework.security.web.DefaultSecurityFilterChain",
	"org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext",
)

private val TO_ERROR = arrayOf(
	"org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
)

val defaultLogLevels = mutableMapOf(
	*TO_WARN.map { it to Level.WARN }.toTypedArray(),
	*TO_ERROR.map { it to Level.ERROR }.toTypedArray()
)
