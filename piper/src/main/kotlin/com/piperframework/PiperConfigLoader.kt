package com.piperframework

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.reflect.KClass

abstract class PiperConfigLoader<Config : com.piperframework.config.Config>(private val clazz: KClass<Config>) {

    protected open val objectMapper = jacksonObjectMapper()

    abstract fun load(): Config

    protected fun loadInternal(fileName: String): Config? {
        val stream = this.javaClass.getResourceAsStream(fileName) ?: return null
        return objectMapper.readValue(stream, clazz.java)
    }
}
