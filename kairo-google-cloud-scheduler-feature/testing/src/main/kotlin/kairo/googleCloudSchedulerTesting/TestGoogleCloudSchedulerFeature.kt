package kairo.googleCloudSchedulerTesting

import com.google.inject.Binder
import com.google.inject.Injector
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.googleCloudScheduler.BaseKairoGoogleCloudSchedulerFeature
import kairo.googleCloudScheduler.JobCreator

public class TestGoogleCloudSchedulerFeature : BaseKairoGoogleCloudSchedulerFeature(), TestFeature.BeforeEach {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<JobCreator>().toInstance(FakeJobCreator())
  }

  override suspend fun beforeEach(injector: Injector) {
    val taskCreator = injector.getInstance<JobCreator>() as FakeJobCreator
    taskCreator.reset()
  }
}
