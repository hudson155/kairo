package kairo.feature

import kairo.server.Server
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.Koin

public interface FeatureTestAware {
  public var ExtensionContext.koin: Koin?
    get() = getStore(FeatureTest.namespace).get<Koin>("koin")
    set(value) {
      getStore(FeatureTest.namespace).put("koin", value)
    }

  public var ExtensionContext.server: Server?
    get() = getStore(FeatureTest.namespace).get<Server>("server")
    set(value) {
      getStore(FeatureTest.namespace).put("server", value)
    }
}
