package io.limberapp.backend.module.auth.testing

import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.backend.test.LimberTestImpl
import com.piperframework.module.TestMongoModule
import com.piperframework.testing.AbstractResourceTest
import com.piperframework.testing.MockedServices
import com.piperframework.testing.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    protected val mockedServices: MockedServices = MockedServices(OrgService::class, UserService::class)

    private val testMongoModule = TestMongoModule()

    override val limberTest = LimberTestImpl(
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
