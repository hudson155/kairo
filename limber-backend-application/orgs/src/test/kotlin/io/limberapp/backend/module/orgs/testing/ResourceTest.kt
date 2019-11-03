package io.limberapp.backend.module.orgs.testing

import io.ktor.application.Application
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.LimberApp
import io.limberapp.framework.config.Config
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.createClient
import io.limberapp.framework.testing.AbstractResourceTest
import org.junit.Before
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

abstract class ResourceTest : AbstractResourceTest() {

    protected val clock: Clock =
        Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))

    private val config = Config(
        database = DatabaseConfig.local("limberapptest"),
        jwt = JwtConfig(requireSignature = false)
    )

    override val limberTest = LimberTest(object : LimberApp(config) {

        override fun getMainModule(application: Application) =
            TestMainModule(application, clock, config)

        override val modules = listOf(OrgsModule())
    })

    @Before
    fun before() {
        val mongoClient = config.createClient()
        mongoClient.getDatabase(config.database.database).drop()
    }
}
