package io.limberapp.backend.config

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.piperframework.PiperConfigLoader
import com.piperframework.jackson.objectMapper.PiperObjectMapper

internal class LimberConfigLoader : PiperConfigLoader<LimberAppMonolithConfig>(LimberAppMonolithConfig::class) {

    override val objectMapper = PiperObjectMapper(YAMLFactory())

    override fun load(): LimberAppMonolithConfig {
        val envString = System.getenv("LIMBERAPP_ENV") ?: "prod"
        val config = loadInternal("/config/$envString.yml") ?: error("Config for LIMBER_ENV=$envString not found.")
        return config.decrypt()
    }
}
