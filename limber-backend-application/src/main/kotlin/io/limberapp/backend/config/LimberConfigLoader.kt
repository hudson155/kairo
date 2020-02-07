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
        val configName = System.getenv(LIMBER_CONFIG) ?: "prod"
        logger.info("Loading config $configName...")
        val config = loadInternal("/config/$configName.yml") ?: error("Config $LIMBER_CONFIG=$configName not found.")
        logger.info("Loaded config $configName.")
        logger.info("Decrypting config $configName...")
        val decryptedConfig = config.decrypt()
        logger.info("Decrypted config $configName.")
        return decryptedConfig
    }
}
