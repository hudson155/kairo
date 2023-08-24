package limber.feature

import com.google.inject.Injector

public inline fun <reified T : Any> Injector.filterBindings(): List<T> =
  bindings.mapNotNull { (key, binding) ->
    if (!T::class.java.isAssignableFrom(key.typeLiteral.rawType)) return@mapNotNull null
    return@mapNotNull binding.provider.get() as T
  }
