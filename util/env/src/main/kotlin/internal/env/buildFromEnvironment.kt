package internal.env

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

private fun getEnvName(annotations: List<Annotation>): String? =
	annotations.firstOrNull { it is Env }?.let { (it as Env).name }

private fun getEnvName(klass: KClass<*>) = getEnvName(klass.annotations)
private fun getEnvName(param: KParameter) = getEnvName(param.annotations)

fun <T : Any> buildFromEnvironment(
	klass: KClass<T>,
	vararg additionalParsers: KFunction<Map<KClass<*>, (String) -> Any>>
): T {
	val parsers = Parsers(*additionalParsers)

	val env = System.getenv()

	val prefix = getEnvName(klass) ?: ""
	val args = HashMap<KParameter, Any?>()

	val constructor = klass.primaryConstructor ?: throw Exception("No primary constructor for '${klass.jvmName}'")

	for (parameter in constructor.parameters) {
		val envName = getEnvName(parameter)?.let { "$prefix$it" }
		if (envName == null && !parameter.isOptional && !parameter.type.isMarkedNullable) {
			throw Exception("Could not construct '${klass.jvmName}' from environment, non-optional parameter not supplied with @Env annotation '${parameter.name}'")
		}
		val envValue = try {
			parsers.parse(parameter.type.jvmErasure, env[envName]?.let { if (it == "unset") null else it })
		} catch (ex: Throwable) {
			throw Exception(
				"Could not constructor '${klass.jvmName}' from environment, problems parsing parameter '${parameter.name}'",
				ex
			)
		}

		if (envValue == null && !parameter.isOptional && !parameter.type.isMarkedNullable) {
			throw Exception("Could not construct '${klass.jvmName}' from environment, missing environment variable '${envName}' with type '${parameter.type.jvmErasure.jvmName}'")
		}

		if (envValue == null && parameter.isOptional) continue
		args[parameter] = envValue
	}

	return constructor.callBy(args)
}
