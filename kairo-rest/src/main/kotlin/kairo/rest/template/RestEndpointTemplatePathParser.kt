package kairo.rest.template

import kairo.rest.Rest
import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplatePath.Component
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

internal object RestEndpointTemplatePathParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(
    kClass: KClass<out RestEndpoint<*, *>>,
    params: List<KParameter>,
  ): RestEndpointTemplatePath {
    val pathParams = params.filter { it.hasAnnotation<RestEndpoint.PathParam>() }
    val annotation = getAnnotation(kClass)
    wrapErrorMessage(kClass) {
      require(annotation.path.startsWith('/')) { "${error.restAnnotation} path is invalid. Must start with a slash" }
      val path =
        if (annotation.path.length == 1) {
          RestEndpointTemplatePath(emptyList())
        } else {
          RestEndpointTemplatePath(annotation.path.drop(1).split('/').map { Component.from(it) })
        }
      validatePath(pathParams, path)
      validatePathParams(pathParams, path)
      return path
    }
  }

  private fun getAnnotation(kClass: KClass<out RestEndpoint<*, *>>): Rest {
    val annotations = kClass.findAnnotations<Rest>()
    require(annotations.isNotEmpty()) {
      "${error.endpoint(kClass)}: Must define ${error.restAnnotation}."
    }
    return annotations.single()
  }

  private inline fun <T> wrapErrorMessage(
    kClass: KClass<out RestEndpoint<*, *>>,
    block: () -> T,
  ): T {
    try {
      return block()
    } catch (e: IllegalArgumentException) {
      val message = e.message?.let { "${error.endpoint(kClass)}: $it." } ?: throw e
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
        "${error.restAnnotation} path is invalid. Duplicate param (param=${component.value})"
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
      val paramName = checkNotNull(param.name)
      require(!param.type.isMarkedNullable) {
        "${error.pathParamAnnotation} must not be nullable (param=$paramName)"
      }
      require(!param.isOptional) {
        "${error.pathParamAnnotation} must not be optional (param=$paramName)"
      }
      require(path.components.any { it is Component.Param && it.value == paramName }) {
        "Unused ${error.pathParamAnnotation} (param=$paramName)"
      }
    }
  }
}
