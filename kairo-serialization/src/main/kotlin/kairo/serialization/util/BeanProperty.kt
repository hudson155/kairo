@file:Suppress("ForbiddenMethodCall")

package kairo.serialization.util

import com.fasterxml.jackson.databind.BeanProperty

public inline fun <reified T : Annotation> BeanProperty.getAnnotation(): T? =
  getAnnotation(T::class.java)
