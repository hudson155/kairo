package kairo.admin.collector

import kairo.admin.model.EndpointInfo
import kairo.admin.model.ParamInfo
import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplate
import kairo.rest.template.RestEndpointTemplatePath
import kotlin.reflect.KClass
import kotlin.reflect.KType
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
        requestBodyFields = extractBodyFields(kClass),
        requestBodyExample = extractBodyExample(kClass),
        responseType = extractResponseType(kClass),
        responseFields = extractResponseFields(kClass),
        endpointClassName = kClass.simpleName ?: kClass.qualifiedName ?: "Unknown",
        qualifiedClassName = kClass.qualifiedName ?: kClass.simpleName ?: "Unknown",
        inputType = extractInputType(kClass),
        isDataObject = kClass.objectInstance != null,
      )
    }

  private fun extractParamType(kClass: KClass<out RestEndpoint<*, *>>, paramName: String): String {
    val constructor = kClass.primaryConstructor ?: return "Unknown"
    val param = constructor.valueParameters.find { it.name == paramName }
    return param?.type?.classifier?.let { (it as? KClass<*>)?.simpleName } ?: "String"
  }

  private fun extractBodyFields(kClass: KClass<out RestEndpoint<*, *>>): List<ParamInfo> {
    val constructor = kClass.primaryConstructor ?: return emptyList()
    val bodyParam = constructor.valueParameters.find { it.name == "body" } ?: return emptyList()
    val bodyClass = bodyParam.type.classifier as? KClass<*> ?: return emptyList()
    return extractClassFields(bodyClass)
  }

  private fun extractResponseFields(kClass: KClass<out RestEndpoint<*, *>>): List<ParamInfo> {
    val responseClass = extractResponseClass(kClass) ?: return emptyList()
    return extractClassFields(responseClass)
  }

  private fun extractClassFields(kClass: KClass<*>): List<ParamInfo> {
    val constructor = kClass.primaryConstructor ?: return emptyList()
    return constructor.valueParameters.map { param ->
      ParamInfo(
        name = param.name ?: "unknown",
        type = formatType(param.type),
        required = !param.type.isMarkedNullable,
      )
    }
  }

  private fun extractResponseClass(kClass: KClass<out RestEndpoint<*, *>>): KClass<*>? {
    val restEndpointType = kClass.supertypes.find {
      (it.classifier as? KClass<*>)?.qualifiedName == RestEndpoint::class.qualifiedName
    } ?: return null
    val responseArg = restEndpointType.arguments.getOrNull(1)?.type ?: return null
    val responseClass = responseArg.classifier as? KClass<*> ?: return null
    if (responseClass.simpleName == "Unit") return null
    // Unwrap List/Set to get the element type.
    if (responseClass.simpleName == "List" || responseClass.simpleName == "Set") {
      val elementType = responseArg.arguments.firstOrNull()?.type ?: return null
      return elementType.classifier as? KClass<*>
    }
    return responseClass
  }

  @Suppress("CyclomaticComplexMethod")
  private fun formatType(type: KType): String {
    val classifier = type.classifier as? KClass<*> ?: return "Unknown"
    val name = classifier.simpleName ?: "Unknown"
    val args = type.arguments
    val formatted = if (args.isNotEmpty()) {
      val argNames = args.mapNotNull { it.type?.let { t -> formatType(t) } }
      if (argNames.isNotEmpty()) "$name<${argNames.joinToString(", ")}>" else name
    } else {
      name
    }
    return if (type.isMarkedNullable) "$formatted?" else formatted
  }

  private fun extractBodyType(kClass: KClass<out RestEndpoint<*, *>>): String? {
    val constructor = kClass.primaryConstructor ?: return null
    val bodyParam = constructor.valueParameters.find { it.name == "body" } ?: return null
    return (bodyParam.type.classifier as? KClass<*>)?.simpleName
  }

  private fun extractBodyExample(kClass: KClass<out RestEndpoint<*, *>>): String? {
    val constructor = kClass.primaryConstructor ?: return null
    val bodyParam = constructor.valueParameters.find { it.name == "body" } ?: return null
    val bodyClass = bodyParam.type.classifier as? KClass<*> ?: return null
    val bodyConstructor = bodyClass.primaryConstructor ?: return null
    val fields = bodyConstructor.valueParameters
    if (fields.isEmpty()) return "{}"
    val lines = fields.map { param ->
      val example = exampleValue(param.type, param.name ?: "value")
      "  \"${param.name}\": $example"
    }
    return "{\n${lines.joinToString(",\n")}\n}"
  }

  @Suppress("CyclomaticComplexMethod")
  private fun exampleValue(type: KType, fieldName: String): String {
    val classifier = type.classifier as? KClass<*>
    val name = classifier?.simpleName ?: return "null"
    val nullable = type.isMarkedNullable
    return when {
      name == "String" && nullable -> "null"
      name == "String" -> "\"${fieldName}\""
      name == "Int" || name == "Long" -> "0"
      name == "Double" || name == "Float" -> "0.0"
      name == "Boolean" -> "false"
      name == "Instant" -> "\"2025-01-01T00:00:00Z\""
      name == "UUID" -> "\"00000000-0000-0000-0000-000000000000\""
      name == "List" || name == "Set" -> "[]"
      name == "Map" -> "{}"
      nullable -> "null"
      else -> "\"${fieldName}\""
    }
  }

  private fun extractInputType(kClass: KClass<out RestEndpoint<*, *>>): String {
    val supertypes = kClass.supertypes
    val restEndpointType = supertypes.find {
      (it.classifier as? KClass<*>)?.qualifiedName == RestEndpoint::class.qualifiedName
    } ?: return "Unknown"
    val inputArg = restEndpointType.arguments.getOrNull(0)
    return (inputArg?.type?.classifier as? KClass<*>)?.simpleName ?: "Unknown"
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
