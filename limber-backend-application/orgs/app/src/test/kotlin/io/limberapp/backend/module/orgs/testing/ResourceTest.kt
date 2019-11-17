package io.limberapp.backend.module.orgs.testing

import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.config.database.DatabaseConfig
import io.limberapp.framework.createClient
import io.limberapp.framework.module.MongoModule
import io.limberapp.framework.testing.AbstractResourceTest
import io.limberapp.framework.testing.LimberTest
import io.limberapp.framework.testing.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    private val databaseConfig = DatabaseConfig.local("limberapptest")

    override val limberTest = LimberTest(
        TestLimberApp(
            config = config,
            module = OrgsModule(),
            additionalModules = listOf(MongoModule(databaseConfig)),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    )

    override fun before() {
        super.before()
        val mongoClient = databaseConfig.createClient()
        mongoClient.getDatabase(databaseConfig.database).drop()
    }
}
