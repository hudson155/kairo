package io.limberapp.common.config

import com.google.common.io.Resources
import io.limberapp.common.serialization.LimberObjectMapper
import kotlin.reflect.KClass

/**
 * Loads configs from YAML files.
 */
object ConfigLoader {
  private val objectMapper: LimberObjectMapper =
      LimberObjectMapper(factory = LimberObjectMapper.Factory.YAML)

  inline fun <reified C : Config> load(configName: String): C = load(configName, C::class)

  fun <C : Config> load(configName: String, configClass: KClass<C>): C {
    val stream = Resources.getResource("config/$configName.yaml")
    return objectMapper.readValue(stream, configClass.java)
  }
}
