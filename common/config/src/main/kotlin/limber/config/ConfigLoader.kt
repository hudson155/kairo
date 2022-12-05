package limber.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.io.Resources
import limber.config.deserializer.ConfigIntegerDeserializer
import limber.config.deserializer.ConfigStringDeserializer
import limber.config.deserializer.ProtectedConfigStringDeserializer
import limber.serialization.ObjectMapperFactory
import limber.type.ProtectedString
import kotlin.reflect.KClass

/**
 * Loads configs from YAML files.
 */
public object ConfigLoader {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Yaml) {
    module.addConfigDeserializers()
  }.build()

  public inline fun <reified C : Config> load(configName: String): C =
    load(configName, C::class)

  public fun <C : Config> load(configName: String, configKClass: KClass<C>): C {
    val config = loadAsJson(configName)
    return objectMapper.convertValue(config, configKClass.java)
  }

  private fun loadAsJson(configName: String): ObjectNode {
    val stream = Resources.getResource("config/$configName.yaml")
    val config = objectMapper.readValue<ObjectNode>(stream)
    val extends = config["extends"].let { extends ->
      extends ?: return@loadAsJson config
      config.remove("extends")
      return@let loadAsJson(extends.textValue())
    }
    return merge(extends, config)
  }

  private fun merge(extends: ObjectNode, config: ObjectNode): ObjectNode {
    config.fields().forEach { (fieldName, fieldValue) ->
      when (fieldValue) {
        is ArrayNode, is ValueNode -> extends.set(fieldName, fieldValue)
        is ObjectNode -> when (objectMapper.convertValue<CascadeType>(fieldValue["type"])) {
          CascadeType.Merge -> {
            val merged = merge(extends[fieldName] as ObjectNode, objectMapper.convertValue(fieldValue["value"]))
            extends.set(fieldName, merged)
          }
          CascadeType.Remove -> extends.remove(fieldName)
          CascadeType.Replace -> extends.set(fieldName, objectMapper.convertValue<JsonNode>(fieldValue["value"]))
        }
        else -> error("Unsupported JsonNode type: ${fieldValue::class.simpleName}.")
      }
    }
    return extends
  }
}

internal fun SimpleModule.addConfigDeserializers() {
  addDeserializer(Int::class.javaObjectType, ConfigIntegerDeserializer())
  addDeserializer(Int::class.javaPrimitiveType, ConfigIntegerDeserializer())
  addDeserializer(ProtectedString::class.javaObjectType, ProtectedConfigStringDeserializer())
  addDeserializer(String::class.javaObjectType, ConfigStringDeserializer())
}
