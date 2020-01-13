package io.limberapp.backend.module.forms.testing

import com.piperframework.module.TestMongoModule
import com.piperframework.module.TestSqlModule
import com.piperframework.testing.AbstractResourceTest
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.test.LimberTest
import io.limberapp.backend.test.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    private val testMongoModule = TestMongoModule()
    private val testSqlModule = TestSqlModule()

    override val piperTest = LimberTest(
        TestLimberApp(
            config = config,
            module = FormsModule(),
            additionalModules = listOf(testMongoModule, testSqlModule),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    )

    override fun before() {
        super.before()
        testMongoModule.dropDatabase()
        testSqlModule.dropDatabase()
    }
}
