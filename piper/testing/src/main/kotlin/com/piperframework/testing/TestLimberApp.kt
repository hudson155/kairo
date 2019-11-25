package com.piperframework.testing

import com.google.inject.AbstractModule
import io.ktor.application.Application
import com.piperframework.LimberApp
import com.piperframework.config.Config
import com.piperframework.module.MainModule
import com.piperframework.module.Module
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import java.time.Clock

class TestLimberApp(
    config: com.piperframework.config.Config,
    module: com.piperframework.module.Module,
    private val additionalModules: List<AbstractModule>,
    private val fixedClock: Clock,
    private val deterministicUuidGenerator: UuidGenerator
) : LimberApp<com.piperframework.config.Config>(config) {

    override fun getMainModules(application: Application): List<AbstractModule> =
        listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)).plus(additionalModules)

    override val modules = listOf(module)
}
