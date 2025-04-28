package kairo.googleCloudSchedulerTesting

import com.google.inject.Binder
import com.google.inject.Injector
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.googleCloudScheduler.BaseKairoGoogleCloudSchedulerFeature
import kairo.googleCloudScheduler.GoogleCloudScheduler

public open class TestGoogleCloudSchedulerFeature : BaseKairoGoogleCloudSchedulerFeature(), TestFeature.BeforeEach {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<GoogleCloudScheduler>().toInstance(FakeGoogleCloudScheduler())
  }

  override suspend fun beforeEach(injector: Injector) {
    val googleCloudScheduler = injector.getInstance<GoogleCloudScheduler>() as FakeGoogleCloudScheduler
    googleCloudScheduler.reset()
  }
}
