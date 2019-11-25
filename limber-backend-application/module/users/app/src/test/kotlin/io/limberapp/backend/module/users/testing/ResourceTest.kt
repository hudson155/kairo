package io.limberapp.backend.module.users.testing

import io.limberapp.backend.module.users.UsersModule
import io.limberapp.backend.test.LimberTest
import com.piperframework.module.TestMongoModule
import com.piperframework.testing.AbstractResourceTest
import com.piperframework.testing.TestPiperApp

abstract class ResourceTest : AbstractResourceTest() {

    private val testMongoModule = TestMongoModule()

    override val piperTest = LimberTest(
        TestPiperApp(
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
