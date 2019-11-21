package io.limberapp.framework.testing

import com.google.inject.AbstractModule
import io.ktor.application.Application
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.module.MainModule
import io.limberapp.framework.module.Module
import io.limberapp.framework.util.uuid.uuidGenerator.UuidGenerator
import java.time.Clock

class TestLimberApp(
    config: Config,
    module: Module,
    private val additionalModules: List<AbstractModule>,
    private val fixedClock: Clock,
    private val deterministicUuidGenerator: UuidGenerator
) : LimberApp<Config>(config) {

    override fun getMainModules(application: Application): List<AbstractModule> =
        listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator))
            .plus(additionalModules)

    override val modules = listOf(module)
}
