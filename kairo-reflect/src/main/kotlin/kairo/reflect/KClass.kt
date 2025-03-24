package kairo.reflect

import kotlin.reflect.KClass

public val <T : Any> KClass<T>.sealedSubclassesRecursive: List<KClass<out T>>
  get() =
    when {
      isFinal -> listOf(this)
      isSealed -> sealedSubclasses.flatMap { it.sealedSubclassesRecursive }
      else -> error("Invalid class: ${simpleName!!}.")
    }
