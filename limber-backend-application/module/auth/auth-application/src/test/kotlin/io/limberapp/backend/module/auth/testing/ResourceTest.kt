package io.limberapp.backend.module.auth.testing

import com.piperframework.testing.MockedServices
import io.limberapp.backend.module.TestSqlModule
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.service.account.AccountService
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.test.LimberResourceTest

abstract class ResourceTest : LimberResourceTest() {
  override val module = AuthModule()

  private val testSqlModule = TestSqlModule()

  protected val mockedServices: MockedServices = MockedServices(
    AccountService::class,
    FeatureService::class,
    OrgService::class,
    UserService::class
  )

  override val additionalModules = setOf(mockedServices, testSqlModule)

  override fun before() {
    testSqlModule.dropDatabase()
  }

  override fun after() {
    testSqlModule.unconfigure()
  }
}
