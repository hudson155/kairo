package com.piperframework.module

import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.module.annotation.Service
import com.piperframework.module.annotation.Store
import kotlin.reflect.KClass

/**
 * A Module encapsulates a distinct concept in the application. It wires up the endpoints and binds services and stores.
 * All endpoints, services, and stores need to be included in exactly 1 module. Modules can use services and stores that
 * are bound in other modules, as long as both modules are included in the same application.
 */
abstract class Module : ModuleWithLifecycle() {

    /**
     * Endpoints are automatically wired up.
     */
    abstract val endpoints: List<Class<out ApiEndpoint<*, *, *>>>

    final override fun configure() {
        configureEndpoints()
        bindServices()
        bindStores()
    }

    final override fun unconfigure() = Unit

    private fun configureEndpoints() {
        endpoints.forEach { bind(it).asEagerSingleton() }
    }

    /**
     * The implementation of this method should bind all necessary services. It's best to bind them as singletons using
     * the bind() method provided.
     */
    protected abstract fun bindServices()

    /**
     * The implementation of this method should bind all necessary stores. It's best to bind them as singletons using
     * the bind() method provided.
     */
    protected abstract fun bindStores()

    protected fun <T : Any> bindService(serviceClass: KClass<T>, implementationClass: KClass<out T>) =
        bind(serviceClass, Service::class, implementationClass)

    protected fun <T : Any> bindStore(serviceClass: KClass<T>, implementationClass: KClass<out T>) =
        bind(serviceClass, Store::class, implementationClass)

    /**
     * This method should be used to bind services and stores.
     */
    private fun <T : Any> bind(
        serviceClass: KClass<T>,
        annotation: KClass<out Annotation>,
        implementationClass: KClass<out T>
    ) {
        bind(serviceClass.java).annotatedWith(annotation.java).to(implementationClass.java).asEagerSingleton()
    }
}
