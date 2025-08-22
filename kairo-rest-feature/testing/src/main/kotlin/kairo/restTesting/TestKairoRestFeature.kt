package kairo.restTesting

import com.google.inject.Binder
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.toClass
import kairo.rest.BaseKairoRestFeature
import kairo.rest.client.RestClient

public open class TestKairoRestFeature : BaseKairoRestFeature() {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<RestClient>().toClass(MockRestClient::class)
  }
}
