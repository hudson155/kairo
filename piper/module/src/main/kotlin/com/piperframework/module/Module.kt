package com.piperframework.module

import com.piperframework.endpoint.ApiEndpoint
import kotlinx.serialization.modules.SerialModule
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * A [Module] encapsulates a distinct concept in the application. It wires up the endpoints and binds services and.
 * stores. All endpoints, services, and stores need to be included in exactly 1 module. Modules can use services and
 * stores that are bound in other modules, as long as both modules are included in the same application.
 */
abstract class Module : ModuleWithLifecycle() {
    private val logger = LoggerFactory.getLogger(Module::class.java)

    /**
     * The serial module is used for serialization.
     */
    abstract val serialModule: SerialModule

    /**
     * Endpoints are automatically wired up.
     */
    abstract val endpoints: List<Class<out ApiEndpoint<*, *, *>>>

    final override fun configure() {
        configureEndpoints()
        bindServices()
    }

    final override fun unconfigure() = Unit

    private fun configureEndpoints() {
        logger.info("Binding ${endpoints.size} endpoints for module ${this::class.simpleName}...")
        endpoints.forEach { bind(it).asEagerSingleton() }
    }

    /**
     * The implementation of this method should bind all necessary services. It's best to bind them as singletons using
     * the bind() method provided.
     */
    protected abstract fun bindServices()

    /**
     * This method should be used to bind services and stores.
     */
    protected fun <T : Any> bind(serviceClass: KClass<T>, implementationClass: KClass<out T>) {
        bind(serviceClass.java).to(implementationClass.java).asEagerSingleton()
    }
}
