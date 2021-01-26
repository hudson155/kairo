package io.limberapp.common.module

import io.limberapp.common.restInterface.EndpointHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

abstract class Feature : Module() {
  private val logger: Logger = LoggerFactory.getLogger(Feature::class.java)

  abstract val apiEndpoints: List<KClass<out EndpointHandler<*, *>>>

  override fun configure() {
    configureEndpoints()
    super.configure()
  }

  private fun configureEndpoints() {
    logger.info("Binding ${apiEndpoints.size} endpoints for feature ${this::class.simpleName}...")
    apiEndpoints.forEach { bind(it.java).asEagerSingleton() }
  }
}
