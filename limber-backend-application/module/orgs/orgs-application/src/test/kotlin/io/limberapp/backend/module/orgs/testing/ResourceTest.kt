package io.limberapp.backend.module.orgs.testing

import com.piperframework.module.TestSqlModule
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.test.LimberResourceTest

abstract class ResourceTest : LimberResourceTest() {
    override val module = OrgsModule()

    private val testSqlModule = TestSqlModule()

    override val additionalModules = setOf(testSqlModule)

    override fun before() {
        testSqlModule.dropDatabase()
    }

    override fun after() {
        testSqlModule.close()
    }
}
