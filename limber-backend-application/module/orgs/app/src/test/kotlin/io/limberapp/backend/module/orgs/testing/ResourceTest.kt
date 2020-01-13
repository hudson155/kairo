package io.limberapp.backend.module.orgs.testing

import com.piperframework.module.TestSqlModule
import com.piperframework.testing.AbstractResourceTest
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.test.LimberTest
import io.limberapp.backend.test.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    private val testSqlModule = TestSqlModule()

    override val piperTest = LimberTest(
        TestLimberApp(
            config = config,
            module = OrgsModule(),
            additionalModules = listOf(testSqlModule),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    )

    override fun before() {
        super.before()
        testSqlModule.dropDatabase()
    }
}
