package io.limberapp.backend.module.orgs.testing

import io.ktor.application.Application
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.testing.AbstractResourceTest

abstract class ResourceTest : AbstractResourceTest() {

    private val config = Config(
        database = DatabaseConfig.local("limberapptest"),
        jwt = JwtConfig(requireSignature = false)
    )

    override val limberTest = LimberTest(object : LimberApp(config) {
        override fun getMainModule(application: Application) = TestMainModule(application, config)
        override val modules = listOf(OrgsModule())
    })
}
