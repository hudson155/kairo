package kairo.dependencyInjection

import com.google.inject.Key
import com.google.inject.name.Names

public inline fun <reified T : Any> key(): Key<T> =
  Key.get(type())

public inline fun <reified T : Any> namedKey(name: String): Key<T> =
  key<T>().withAnnotation(Names.named(name))
