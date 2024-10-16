package kairo.dependencyInjection

import com.google.inject.Key

public inline fun <reified T : Any> key(): Key<T> =
  Key.get(type())
