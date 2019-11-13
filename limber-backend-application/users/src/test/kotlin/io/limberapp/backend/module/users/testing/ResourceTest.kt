package io.limberapp.backend.module.users.testing

import io.ktor.application.Application
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.config.serving.StaticFiles
import io.limberapp.framework.createClient
import io.limberapp.framework.testing.AbstractResourceTest

abstract class ResourceTest : AbstractResourceTest() {

    private val config = Config(
        serving = ServingConfig(
            apiPathPrefix = "/",
            staticFiles = StaticFiles(false)
        ),
        database = DatabaseConfig.local("limberapptest"),
        jwt = JwtConfig(requireSignature = false)
    )

    override val limberTest = LimberTest(object : LimberApp(config) {

        override fun getMainModule(application: Application) =
            TestMainModule(application, fixedClock, deterministicUuidGenerator, config)

        override val modules = listOf(UsersModule())
    })

    override fun before() {
        super.before()
        val mongoClient = config.createClient()
        mongoClient.getDatabase(config.database.database).drop()
    }
}
