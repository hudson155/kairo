package io.limberapp.backend.module.users.testing

import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.framework.module.TestMongoModule
import io.limberapp.framework.testing.AbstractResourceTest
import io.limberapp.framework.testing.LimberTest
import io.limberapp.framework.testing.MockedServices
import io.limberapp.framework.testing.TestLimberApp

abstract class ResourceTest : AbstractResourceTest() {

    protected val mockedServices: MockedServices =
        MockedServices(listOf(OrgService::class, UserService::class))

    private val testMongoModule = TestMongoModule()

    override val limberTest = LimberTest(
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
