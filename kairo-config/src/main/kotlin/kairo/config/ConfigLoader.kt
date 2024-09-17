package kairo.config

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.io.Resources
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.commandRunner.DefaultCommandRunner
import kairo.commandRunner.NoopCommandRunner
import kairo.environmentVariableSupplier.DefaultEnvironmentVariableSupplier
import kairo.environmentVariableSupplier.StaticEnvironmentVariableSupplier
import kairo.gcpSecretSupplier.DefaultGcpSecretSupplier
import kairo.gcpSecretSupplier.NoopGcpSecretSupplier
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * Loads configs from YAML files, with support for config extension and application,
 * as well as various sources.
 *
 * Config extension: Configs can extend other configs
 * by specifying "extends: other-config-name" as a top-level YAML property.
 *
 * Config application: Configs can apply other configs
 * by specifying "apply: [other-config-name-0, other-config-name-1]" as a top-level YAML property
 *
 * Implementation note: This class uses [ObjectNode.remove] and [ObjectNode.set],
 * which mutate the underlying object.
 * Understanding this makes the code easier to follow.
 */
public class ConfigLoader(
  private val config: ConfigLoaderConfig,
) {
  private val mapper: JsonMapper =
    ObjectMapperFactory.builder(
      format = ObjectMapperFormat.Yaml,
      modules = listOf(ConfigLoaderModule(config)),
    ).build()

  public inline fun <reified C : Any> load(configName: String? = null): C =
    load(configName, C::class)

  public fun <C : Any> load(configName: String?, configKClass: KClass<C>): C {
    val actualConfigName = getActualConfigName(configName)
    logger.info { "Loading config: $actualConfigName." }
    val config = loadAsJson(actualConfigName)
    @Suppress("ForbiddenMethodCall")
    return mapper.convertValue(config, configKClass.java)
  }

  /**
   * If a config name is provided, use it.
   * Otherwise, fall back to the KAIRO_CONFIG environment variable.
   */
  private fun getActualConfigName(configName: String?): String {
    if (configName != null) return configName

    val environmentVariableName = "KAIRO_CONFIG"
    logger.info { "Getting config name from $environmentVariableName environment variable." }
    return config.environmentVariableSupplier.get(environmentVariableName)
      ?: error("Config name was not provided and $environmentVariableName is not set.")
  }

  /**
   * Loads the config as [ObjectNode] (JSON), processing extension and applications.
   */
  private fun loadAsJson(configName: String): ObjectNode {
    val config = simpleLoadAsJson(configName)
    val extends = extractExtends(config)
    val apply = extractApply(config)
    var result = config
    if (extends != null) {
      result = loadAsJson(extends)
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

  /**
   * Loads the config as [ObjectNode] (JSON) WITHOUT processing extension and applications.
   */
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

  public companion object {
    public fun createDefault(): ConfigLoader =
      ConfigLoader(
        ConfigLoaderConfig(
          commandRunner = DefaultCommandRunner,
          environmentVariableSupplier = DefaultEnvironmentVariableSupplier,
          gcpSecretSupplier = DefaultGcpSecretSupplier,
        ),
      )

    public fun noop(): ConfigLoader =
      ConfigLoader(
        ConfigLoaderConfig(
          commandRunner = NoopCommandRunner,
          environmentVariableSupplier = StaticEnvironmentVariableSupplier(
            mapOf("KAIRO_ALLOW_INSECURE_CONFIG_SOURCES" to false.toString()),
          ),
          gcpSecretSupplier = NoopGcpSecretSupplier,
        ),
      )
  }
}
