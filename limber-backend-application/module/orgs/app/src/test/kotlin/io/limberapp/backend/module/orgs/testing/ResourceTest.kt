package io.limberapp.backend.module.orgs.testing

import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.test.LimberTest
import com.piperframework.module.TestMongoModule
import com.piperframework.testing.AbstractResourceTest
import com.piperframework.testing.TestPiperApp

abstract class ResourceTest : AbstractResourceTest() {

    private val testMongoModule = TestMongoModule()

    override val piperTest = LimberTest(
        TestPiperApp(
            config = config,
            module = OrgsModule(),
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
