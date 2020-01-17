package com.piperframework

import com.piperframework.jackson.objectMapper.PiperObjectMapper
import kotlin.reflect.KClass

abstract class PiperConfigLoader<Config : com.piperframework.config.Config>(private val clazz: KClass<Config>) {

    protected open val objectMapper = PiperObjectMapper()

    private fun getEnvironmentVariable(name: String): String? = System.getenv(name)

    abstract fun load(): Config

    protected fun loadInternal(fileName: String): Config? {
        val stream = this.javaClass.getResourceAsStream(fileName) ?: return null
        return objectMapper.readValue(stream, clazz.java)
    }
}
