package io.limberapp.backend.module.forms.testing

import com.piperframework.module.TestSqlModule
import com.piperframework.testing.AbstractResourceTest
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.test.LimberTest
import io.limberapp.backend.test.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    private val testSqlModule = TestSqlModule()

    override val piperTest = LimberTest {
        TestLimberApp(
            application = this,
            config = config,
            module = FormsModule(),
            additionalModules = listOf(testSqlModule),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    }

    override fun before() {
        super.before()
        testSqlModule.dropDatabase()
    }

    override fun after() {
        super.after()
        testSqlModule.close()
    }
}
