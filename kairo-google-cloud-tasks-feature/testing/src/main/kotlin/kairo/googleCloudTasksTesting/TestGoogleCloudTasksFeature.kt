package kairo.googleCloudTasksTesting

import com.google.inject.Binder
import com.google.inject.Injector
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.googleCloudTasks.BaseKairoGoogleCloudTasksFeature
import kairo.googleCloudTasks.GoogleCloudTasks

public class TestGoogleCloudTasksFeature : BaseKairoGoogleCloudTasksFeature(), TestFeature.BeforeEach {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<GoogleCloudTasks>().toInstance(FakeGoogleCloudTasks())
  }

  override suspend fun beforeEach(injector: Injector) {
    val googleCloudTasks = injector.getInstance<GoogleCloudTasks>() as FakeGoogleCloudTasks
    googleCloudTasks.reset()
  }
}
