package io.limberapp.backend.config

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.piperframework.PiperConfigLoader
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import org.slf4j.LoggerFactory

private const val LIMBER_CONFIG = "LIMBER_CONFIG"

internal class LimberConfigLoader : PiperConfigLoader<LimberAppMonolithConfig>(LimberAppMonolithConfig::class) {

    private val logger = LoggerFactory.getLogger(LimberConfigLoader::class.java)

    override val objectMapper = PiperObjectMapper(YAMLFactory())

    override fun load(): LimberAppMonolithConfig {
        logger.info("Loading config...")
        val envString = System.getenv(LIMBER_CONFIG) ?: "prod"
        logger.info("Loading config for environment $envString...")
        val config = loadInternal("/config/$envString.yml") ?: error("Config for $LIMBER_CONFIG=$envString not found.")
        logger.info("Loaded config for environment $envString.")
        logger.info("Decrypting config for environment $envString...")
        val decryptedConfig = config.decrypt()
        logger.info("Decrypted config for environment $envString.")
        return decryptedConfig
    }
}
