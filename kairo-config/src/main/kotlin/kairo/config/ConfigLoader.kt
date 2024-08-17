package kairo.config

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.io.Resources
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlin.reflect.KClass

/**
 * Loads configs from YAML files, with support for config extension and application.
 *
 * Config extension: Configs can extend other configs
 * by specifying "extends: other-config-name" as a top-level YAML property.
 *
 * Config application: Configs can apply other configs
 * by specifying "apply: [other-config-name-0, other-config-name-1]" as a top-level YAML property
 */
public object ConfigLoader {
  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Yaml).build()

  public inline fun <reified C : Any> load(configName: String): C =
    load(configName, C::class)

  public fun <C : Any> load(configName: String, configKClass: KClass<C>): C {
    val config = loadAsJson(configName)
    return mapper.convertValue(config, configKClass.java)
  }

  private fun loadAsJson(configName: String): ObjectNode {
    val config = simpleLoadAsJson(configName)
    val extends = extractExtends(config)
    val apply = extractApply(config)
    var result = config
    if (extends != null) {
      result = simpleLoadAsJson(extends)
    }
    apply?.forEach { merge(result, loadAsJson(it)) }
    if (extends != null) {
      merge(result, config)
    }
    return result
  }

  private fun extractExtends(config: ObjectNode): String? {
    val jsonNode = config.remove("extends") ?: return null
    return mapper.convertValue<String>(jsonNode)
  }

  private fun extractApply(config: ObjectNode): List<String>? {
    val jsonNode = config.remove("apply") ?: return null
    return (jsonNode as ArrayNode).map { mapper.convertValue<String>(it) }
  }

  private fun simpleLoadAsJson(configName: String): ObjectNode {
    val stream = Resources.getResource("config/$configName.yaml")
    return mapper.readValue<ObjectNode>(stream)
  }

  private fun merge(extends: ObjectNode, config: ObjectNode) {
    config.fields().forEach { (name, jsonNode) ->
      when (jsonNode) {
        is ValueNode -> mergeValueNode(extends, name, jsonNode)
        is ObjectNode -> mergeObjectNode(extends, name, jsonNode)
        else -> throw IllegalArgumentException("Unsupported JsonNode: $jsonNode.")
      }
    }
  }

  private fun mergeValueNode(extends: ObjectNode, name: String, jsonNode: ValueNode) {
    extends.set<ObjectNode>(name, jsonNode)
  }

  private fun mergeObjectNode(extends: ObjectNode, name: String, jsonNode: ObjectNode) {
    when (val mergeType = mapper.convertValue<MergeType>(jsonNode)) {
      is MergeType.Merge -> merge(extends[name] as ObjectNode, mergeType.value)
      is MergeType.Remove -> extends.remove(name)
      is MergeType.Replace -> extends.set<ObjectNode>(name, mergeType.value)
    }
  }
}
