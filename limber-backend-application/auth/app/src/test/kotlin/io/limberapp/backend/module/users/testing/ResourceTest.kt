package io.limberapp.backend.module.users.testing

import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.framework.testing.AbstractResourceTest
import io.limberapp.framework.testing.LimberTest
import io.limberapp.framework.testing.MockedServices
import io.limberapp.framework.testing.TestLimberApp
import kotlin.reflect.KClass

abstract class ResourceTest(
    servicesToMock: List<KClass<*>> = emptyList()
) : AbstractResourceTest() {

    protected val mockedServices: MockedServices = MockedServices(servicesToMock)

    override val limberTest = LimberTest(
        TestLimberApp(
            config = config,
            module = AuthModule(),
            additionalModules = listOf(mockedServices),
            fixedClock = fixedClock,
            deterministicUuidGenerator = deterministicUuidGenerator
        )
    )
}
