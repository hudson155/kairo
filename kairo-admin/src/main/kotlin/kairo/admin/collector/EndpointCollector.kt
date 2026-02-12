package kairo.admin.collector

import kairo.admin.model.EndpointInfo
import kairo.admin.model.ParamInfo
import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplate
import kairo.rest.template.RestEndpointTemplatePath
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

public class EndpointCollector(
  private val endpointClasses: List<KClass<out RestEndpoint<*, *>>>,
) {
  public fun collect(): List<EndpointInfo> =
    endpointClasses.map { kClass ->
      val template = RestEndpointTemplate.from(kClass)
      EndpointInfo(
        method = template.method.value,
        path = template.path.toString(),
        pathParams = template.path.components
          .filterIsInstance<RestEndpointTemplatePath.Component.Param>()
          .map { ParamInfo(it.value, extractParamType(kClass, it.value)) },
        queryParams = template.query.params
          .map { ParamInfo(it.value, extractParamType(kClass, it.value), it.required) },
        contentType = template.contentType?.toString(),
        accept = template.accept?.toString(),
        requestBodyType = extractBodyType(kClass),
        responseType = extractResponseType(kClass),
        endpointClassName = kClass.simpleName ?: kClass.qualifiedName ?: "Unknown",
      )
    }

  private fun extractParamType(kClass: KClass<out RestEndpoint<*, *>>, paramName: String): String {
    val constructor = kClass.primaryConstructor ?: return "Unknown"
    val param = constructor.valueParameters.find { it.name == paramName }
    return param?.type?.classifier?.let { (it as? KClass<*>)?.simpleName } ?: "String"
  }

  private fun extractBodyType(kClass: KClass<out RestEndpoint<*, *>>): String? {
    val constructor = kClass.primaryConstructor ?: return null
    val bodyParam = constructor.valueParameters.find { it.name == "body" } ?: return null
    return (bodyParam.type.classifier as? KClass<*>)?.simpleName
  }

  private fun extractResponseType(kClass: KClass<out RestEndpoint<*, *>>): String {
    val supertypes = kClass.supertypes
    val restEndpointType = supertypes.find {
      (it.classifier as? KClass<*>)?.qualifiedName == RestEndpoint::class.qualifiedName
    } ?: return "Unknown"
    val responseArg = restEndpointType.arguments.getOrNull(1)
    return (responseArg?.type?.classifier as? KClass<*>)?.simpleName ?: "Unknown"
  }
}
