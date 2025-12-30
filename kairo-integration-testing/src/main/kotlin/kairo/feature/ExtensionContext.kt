package kairo.feature

import org.junit.jupiter.api.extension.ExtensionContext

public inline fun <reified T : Any> ExtensionContext.Store.get(key: String): T? =
  get(key, T::class.java)
