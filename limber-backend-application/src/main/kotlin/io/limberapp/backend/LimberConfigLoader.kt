package io.limberapp.backend

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.piperframework.PiperConfigLoader
import com.piperframework.jackson.objectMapper.PiperObjectMapper

internal class LimberConfigLoader : PiperConfigLoader<Config>(Config::class) {

    override val objectMapper = PiperObjectMapper(YAMLFactory())

    override fun load(): Config {
        val envString = System.getenv("LIMBERAPP_ENV") ?: "prod"
        return loadInternal("/config/$envString.yml") ?: error("Config for LIMBER_ENV=$envString not found.")
    }
}
