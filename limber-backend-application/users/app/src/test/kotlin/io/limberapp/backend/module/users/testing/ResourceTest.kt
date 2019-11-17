package io.limberapp.backend.module.users.testing

import io.limberapp.backend.module.users.UsersModule
import io.limberapp.framework.module.TestMongoModule
import io.limberapp.framework.testing.AbstractResourceTest
import io.limberapp.framework.testing.LimberTest
import io.limberapp.framework.testing.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    private val testMongoModule = TestMongoModule()

    override val limberTest = LimberTest(
        TestLimberApp(
            config = config,
            module = UsersModule(),
            additionalModules = listOf(testMongoModule),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    )

    override fun before() {
        super.before()
        testMongoModule.dropDatabase()
    }
}
