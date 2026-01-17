package kairo.dependencyInjection

import kairo.dependencyInjection.KoinExtension.Companion.namespace
import kairo.testing.get
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication

public open class KoinExtension : BeforeEachCallback, ParameterResolver {
  override fun beforeEach(context: ExtensionContext) {
    val koinApplication = koinApplication()
    context.koin = koinApplication
  }

  override fun supportsParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext,
  ): Boolean {
    val koin = checkNotNull(extensionContext.koin)
    return when (val kClass = parameterContext.parameter.type.kotlin) {
      Koin::class -> true
      KoinApplication::class -> true
      else -> koin.koin.getOrNull<Any>(kClass) != null
    }
  }

  override fun resolveParameter(
    parameterContext: ParameterContext,
    extensionContext: ExtensionContext,
  ): Any {
    val koin = checkNotNull(extensionContext.koin)
    return when (val kClass = parameterContext.parameter.type.kotlin) {
      Koin::class -> koin.koin
      KoinApplication::class -> koin
      else -> koin.koin.get(kClass)
    }
  }

  public companion object {
    public val namespace: ExtensionContext.Namespace =
      ExtensionContext.Namespace.create(KoinExtension::class)
  }
}

public var ExtensionContext.koin: KoinApplication?
  get() = getStore(namespace).get<KoinApplication>("koin")
  set(value) {
    getStore(namespace).put("koin", value)
  }
