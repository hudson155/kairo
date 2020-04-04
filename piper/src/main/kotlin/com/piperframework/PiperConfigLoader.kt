package com.piperframework

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlin.reflect.KClass

abstract class PiperConfigLoader<Config : com.piperframework.config.Config>(private val klass: KClass<Config>) {

    protected open val objectMapper = ObjectMapper().registerKotlinModule()

    abstract fun load(): Config

    protected fun loadInternal(fileName: String): Config? {
        val stream = this.javaClass.getResourceAsStream(fileName) ?: return null
        return objectMapper.readValue(stream, klass.java)
    }
}
