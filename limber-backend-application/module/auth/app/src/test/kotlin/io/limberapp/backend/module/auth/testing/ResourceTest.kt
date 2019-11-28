package io.limberapp.backend.module.auth.testing

import com.piperframework.module.TestMongoModule
import com.piperframework.testing.AbstractResourceTest
import com.piperframework.testing.MockedServices
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.backend.test.LimberTest
import io.limberapp.backend.test.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    protected val mockedServices: MockedServices = MockedServices(OrgService::class, UserService::class)

    private val testMongoModule = TestMongoModule()

    override val piperTest = LimberTest(
        TestLimberApp(
            config = config,
            module = AuthModule(),
            additionalModules = listOf(mockedServices, testMongoModule),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    )

    override fun before() {
        super.before()
        testMongoModule.dropDatabase()
    }
}
