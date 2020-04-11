package io.limberapp.backend.module.forms.testing

import com.piperframework.module.TestSqlModule
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.test.LimberResourceTest

abstract class ResourceTest : LimberResourceTest() {

    override val module = FormsModule()

    private val testSqlModule = TestSqlModule()

    override val additionalModules = setOf(testSqlModule)

    override fun before() {
        testSqlModule.dropDatabase()
    }

    override fun after() {
        testSqlModule.close()
    }
}
