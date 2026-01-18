package kairo.dependencyInjection

import kairo.testing.get
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.KoinApplication

public interface KoinExtensionAware {
  public var ExtensionContext.koin: KoinApplication?
    get() = getStore(KoinExtension.namespace).get<KoinApplication>("koin")
    set(value) {
      getStore(KoinExtension.namespace).put("koin", value)
    }
}
