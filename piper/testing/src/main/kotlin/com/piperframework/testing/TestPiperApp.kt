package com.piperframework.testing

import com.piperframework.SimplePiperApp
import com.piperframework.config.Config
import com.piperframework.module.MainModule
import com.piperframework.module.Module
import com.piperframework.module.ModuleWithLifecycle
import com.piperframework.util.uuid.UuidGenerator
import io.ktor.application.Application
import java.time.Clock

abstract class TestPiperApp(
    application: Application,
    config: Config,
    module: Module,
    private val additionalModules: List<ModuleWithLifecycle>,
    private val fixedClock: Clock,
    private val deterministicUuidGenerator: UuidGenerator
) : SimplePiperApp<Config>(application, config) {

    override fun getMainModules(application: Application) =
        listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)).plus(additionalModules)

    override val modules = listOf(module)
}
