package io.limberapp.backend.module.users.testing

import com.piperframework.module.TestSqlModule
import com.piperframework.testing.MockedServices
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.backend.test.LimberResourceTest

abstract class ResourceTest : LimberResourceTest() {

    override val module = UsersModule()

    private val testSqlModule = TestSqlModule()

    protected val mockedServices: MockedServices = MockedServices(OrgService::class)

    override val additionalModules = setOf(mockedServices, testSqlModule)

    override fun before() {
        testSqlModule.dropDatabase()
    }

    override fun after() {
        testSqlModule.close()
    }
}
