package com.piperframework.testing

import com.google.inject.AbstractModule
import com.piperframework.PiperApp
import com.piperframework.config.Config
import com.piperframework.module.MainModule
import com.piperframework.module.Module
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.ktor.application.Application
import java.time.Clock

abstract class TestPiperApp(
    config: Config,
    module: Module,
    private val additionalModules: List<AbstractModule>,
    private val fixedClock: Clock,
    private val deterministicUuidGenerator: UuidGenerator
) : PiperApp<Config>(config) {

    override fun getMainModules(application: Application): List<AbstractModule> =
        listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)).plus(additionalModules)

    override val modules = listOf(module)
}
