package io.limberapp.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object ConfigLoader {
  private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

  inline fun <reified C : Config> load(configName: String) = load(configName, C::class.java)

  fun <C : Config> load(configName: String, configClass: Class<C>): C {
    val stream = this.javaClass.getResourceAsStream("/config/$configName.yaml")
        ?: error("Config $configName not found.")
    return objectMapper.readValue(stream, configClass)
  }
}
