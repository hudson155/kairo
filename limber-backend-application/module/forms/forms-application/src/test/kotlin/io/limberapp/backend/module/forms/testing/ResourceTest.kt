package io.limberapp.backend.module.forms.testing

import com.piperframework.testing.MockedServices
import io.limberapp.backend.module.TestSqlModule
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.backend.test.LimberResourceTest

abstract class ResourceTest : LimberResourceTest() {
  override val module = FormsModule()

  private val testSqlModule = TestSqlModule(config.sqlDatabase)

  protected val mockedServices: MockedServices = MockedServices(FeatureService::class, UserService::class)

  override val additionalModules = setOf(mockedServices, testSqlModule)

  override fun before() {
    testSqlModule.dropDatabase()
  }

  override fun after() {
    testSqlModule.unconfigure()
  }
}
