package io.limberapp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlin.reflect.KClass

object ConfigLoader {
  private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

  fun <C : Config> load(configName: String, kClass: KClass<C>): C {
    val stream = this.javaClass.getResourceAsStream("/config/$configName.yaml")
      ?: error("Config $configName not found.")
    return objectMapper.readValue(stream, kClass.java)
  }
}
