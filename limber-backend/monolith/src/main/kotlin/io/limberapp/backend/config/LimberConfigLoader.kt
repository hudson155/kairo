package io.limberapp.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.limberapp.common.LimberConfigLoader
import org.slf4j.LoggerFactory

private const val LIMBER_CONFIG = "LIMBER_CONFIG"

class LimberConfigLoader : LimberConfigLoader<LimberAppMonolithConfig>(LimberAppMonolithConfig::class) {
  private val logger = LoggerFactory.getLogger(LimberConfigLoader::class.java)

  override val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

  override fun load() = load(null)

  fun load(limberConfig: String?): LimberAppMonolithConfig {
    logger.info("Loading config...")
    val configName = requireNotNull(System.getenv(LIMBER_CONFIG) ?: limberConfig) {
      "Environment variable $LIMBER_CONFIG not set."
    }
    logger.info("Loading config $configName...")
    val config = loadInternal("/config/$configName.yaml") ?: error("Config $LIMBER_CONFIG=$configName not found.")
    logger.info("Loaded config $configName.")
    logger.info("Decrypting config $configName...")
    val decryptedConfig = config.decrypt()
    logger.info("Decrypted config $configName.")
    return decryptedConfig
  }
}
