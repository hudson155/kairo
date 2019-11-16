package io.limberapp.backend.module.orgs.testing

import io.ktor.application.Application
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.config.serving.StaticFiles
import io.limberapp.framework.createClient
import io.limberapp.framework.module.MainModule
import io.limberapp.framework.module.MongoModule
import io.limberapp.framework.testing.AbstractResourceTest

abstract class ResourceTest : AbstractResourceTest() {

    private val config = object : Config {
        override val serving = ServingConfig(
            apiPathPrefix = "/",
            staticFiles = StaticFiles(false)
        )
        override val jwt = JwtConfig(requireSignature = false)
    }
    private val databaseConfig = DatabaseConfig.local("limberapptest")

    override val limberTest = LimberTest(object : LimberApp<Config>(config) {

        override fun getMainModules(application: Application) = listOf(
            MainModule(application, fixedClock, config, deterministicUuidGenerator),
            MongoModule(databaseConfig)
        )

        override val modules = listOf(OrgsModule())
    })

    override fun before() {
        super.before()
        val mongoClient = databaseConfig.createClient()
        mongoClient.getDatabase(databaseConfig.database).drop()
    }
}
