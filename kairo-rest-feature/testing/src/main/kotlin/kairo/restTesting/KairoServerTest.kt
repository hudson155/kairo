package kairo.restTesting

import kairo.dependencyInjection.getInstance
import kairo.featureTesting.KairoServerTest
import kairo.rest.RestClient

public val KairoServerTest.client: RestClient
  get() = injector.getInstance<RestClient>()
