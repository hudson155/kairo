package io.limberapp.backend.module.users.testing

import com.piperframework.module.TestSqlModule
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.backend.test.LimberResourceTest

abstract class ResourceTest : LimberResourceTest() {

    override val module = UsersModule()

    private val testSqlModule = TestSqlModule()

    override val additionalModules = setOf(testSqlModule)

    override fun before() {
        testSqlModule.dropDatabase()
    }

    override fun after() {
        testSqlModule.unconfigure()
    }
}
