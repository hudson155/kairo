package limber.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources
import limber.serialization.ObjectMapperFactory
import kotlin.reflect.KClass

/**
 * Loads configs from YAML files.
 */
public object ConfigLoader {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.YAML).build()

  public inline fun <reified C : Config> load(configName: String): C =
    load(configName, C::class)

  public fun <C : Config> load(configName: String, configKClass: KClass<C>): C {
    val stream = Resources.getResource("config/$configName.yaml")
    return objectMapper.readValue(stream, configKClass.java)
  }
}
