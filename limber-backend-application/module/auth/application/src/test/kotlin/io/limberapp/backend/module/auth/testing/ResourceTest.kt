package io.limberapp.backend.module.auth.testing

import com.piperframework.module.TestSqlModule
import com.piperframework.testing.AbstractResourceTest
import com.piperframework.testing.MockedServices
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.service.account.AccountService
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.test.LimberTest
import io.limberapp.backend.test.TestLimberApp

abstract class ResourceTest : AbstractResourceTest(AuthModule()) {

    protected val mockedServices: MockedServices = MockedServices(
        AccountService::class,
        OrgService::class,
        UserService::class
    )

    private val testSqlModule = TestSqlModule()

    override val piperTest = LimberTest {
        TestLimberApp(
            application = this,
            config = config,
            module = module,
            additionalModules = listOf(mockedServices, testSqlModule),
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
