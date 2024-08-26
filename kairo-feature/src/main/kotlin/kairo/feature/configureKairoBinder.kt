package kairo.feature

import com.google.inject.Binder

public fun Binder.configureKairoBinder() {
  disableCircularProxies()
  requireAtInjectOnConstructors()
  requireExactBindingAnnotations()
}
