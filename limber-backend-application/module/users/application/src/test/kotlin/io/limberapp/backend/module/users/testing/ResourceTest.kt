package io.limberapp.backend.module.users.testing

import com.piperframework.module.TestSqlModule
import com.piperframework.testing.AbstractResourceTest
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.backend.test.LimberTest
import io.limberapp.backend.test.TestLimberApp

abstract class ResourceTest : AbstractResourceTest(UsersModule()) {

    private val testSqlModule = TestSqlModule()

    override val piperTest = LimberTest {
        TestLimberApp(
            application = this,
            config = config,
            module = module,
            additionalModules = listOf(testSqlModule),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    }

    override fun before() {
        testSqlModule.dropDatabase()
    }

    override fun after() {
        testSqlModule.close()
    }
}
