package io.limberapp.framework.module

import com.google.inject.AbstractModule
import io.limberapp.framework.endpoint.ApiEndpoint
import kotlin.reflect.KClass

/**
 * A Module encapsulates a distinct concept in the application. It wires up the endpoints and binds
 * services and stores. All endpoints, services, and stores need to be included in exactly 1 module.
 * Modules can use services and stores that are bound in other modules, as long as both modules are
 * included in the same application.
 */
abstract class Module : AbstractModule() {

    /**
     * Endpoints are automatically wired up.
     */
    abstract val endpoints: List<Class<out ApiEndpoint<*, *>>>

    final override fun configure() {
        configureEndpoints()
        bindServices()
        bindStores()
    }

    private fun configureEndpoints() {
        endpoints.forEach { bind(it).asEagerSingleton() }
    }

    /**
     * The implementation of this method should bind all necessary services. It's best to bind them
     * as singletons using the bind() method provided.
     */
    protected abstract fun bindServices()

    /**
     * The implementation of this method should bind all necessary stores. It's best to bind them as
     * singletons using
     * the bind() method provided.
     */
    protected abstract fun bindStores()

    /**
     * This method should be used to bind services and stores.
     */
    protected fun <T : Any> bind(serviceClass: KClass<T>, implementationClass: KClass<out T>) {
        bind(serviceClass.java).to(implementationClass.java).asEagerSingleton()
    }
}
