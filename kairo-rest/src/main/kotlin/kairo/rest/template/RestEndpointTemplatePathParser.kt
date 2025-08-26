package kairo.rest.template

import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplatePath.Component
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

internal object RestEndpointTemplatePathParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(
    endpoint: KClass<out RestEndpoint<*, *>>,
    params: List<KParameter>,
  ): RestEndpointTemplatePath {
    val pathParams = params.filter { it.hasAnnotation<RestEndpoint.PathParam>() }
    val annotation = getAnnotation(endpoint)
    wrapErrorMessage(endpoint) {
      require(annotation.value.startsWith('/')) { "${error.pathAnnotation} is invalid. Must start with a slash" }
      val path =
        if (annotation.value.length == 1) {
          RestEndpointTemplatePath(emptyList())
        } else {
          RestEndpointTemplatePath(annotation.value.drop(1).split('/').map { Component.from(it) })
        }
      validatePath(pathParams, path)
      validatePathParams(pathParams, path)
      return path
    }
  }

  private fun getAnnotation(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpoint.Path {
    val annotations = endpoint.findAnnotations<RestEndpoint.Path>()
    require(annotations.isNotEmpty()) {
      "${error.endpoint(endpoint)}: Must define ${error.pathAnnotation}."
    }
    val annotation = annotations.singleOrNull()
    requireNotNull(annotation) {
      "${error.endpoint(endpoint)}: Cannot define multiple of ${error.pathAnnotation}."
    }
    return annotation
  }

  private inline fun <T> wrapErrorMessage(
    endpoint: KClass<out RestEndpoint<*, *>>,
    block: () -> T,
  ): T {
    try {
      return block()
    } catch (e: IllegalArgumentException) {
      val message = e.message?.let { "${error.endpoint(endpoint)}: $it." } ?: throw e
      throw IllegalArgumentException(message, e)
    }
  }

  private fun validatePath(
    pathParams: List<KParameter>,
    path: RestEndpointTemplatePath,
  ) {
    path.components.forEachIndexed { i, component ->
      if (component !is Component.Param) return@forEachIndexed
      require(path.components.take(i).none { it is Component.Param && it.value == component.value }) {
        "${error.pathAnnotation} is invalid. Duplicate param (param=${component.value})"
      }
      require(pathParams.any { it.name == component.value }) {
        "Missing ${error.pathParamAnnotation} (param=${component.value})"
      }
    }
  }

  private fun validatePathParams(
    pathParams: List<KParameter>,
    path: RestEndpointTemplatePath,
  ) {
    pathParams.forEach { param ->
      require(!param.type.isMarkedNullable) {
        "${error.pathParamAnnotation} must not be nullable (param=${param.name})"
      }
      require(!param.isOptional) {
        "${error.pathParamAnnotation} must not be optional (param=${param.name})"
      }
      require(path.components.any { it is Component.Param && it.value == param.name }) {
        "Unused ${error.pathParamAnnotation} (param=${param.name})"
      }
    }
  }
}
