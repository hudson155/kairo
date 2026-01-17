package kairo.feature

import kairo.server.Server
import kairo.testing.get
import org.junit.jupiter.api.extension.ExtensionContext

public interface FeatureTestAware {
  public var ExtensionContext.server: Server?
    get() = getStore(FeatureTest.namespace).get<Server>("server")
    set(value) {
      getStore(FeatureTest.namespace).put("server", value)
    }
}
