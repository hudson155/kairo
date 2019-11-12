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
import io.limberapp.framework.util.uuidGenerator.DeterministicUuidGenerator
import org.junit.Before
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

abstract class ResourceTest : AbstractResourceTest() {

    protected val clock: Clock =
        Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("America/New_York"))

    protected val uuidGenerator = DeterministicUuidGenerator()

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
            TestMainModule(application, clock, uuidGenerator, config)

        override val modules = listOf(UsersModule())
    })

    @Before
    fun before() {
        val mongoClient = config.createClient()
        mongoClient.getDatabase(config.database.database).drop()
        uuidGenerator.reset()
    }
}
