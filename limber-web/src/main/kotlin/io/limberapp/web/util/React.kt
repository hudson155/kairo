package io.limberapp.web.util

import react.*

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal fun buildElements(handler: RBuilder.() -> Unit) = react.buildElements(handler) as ReactElement?

internal fun useIsMounted(): RMutableRef<Boolean> {
  val isMounted = useRef(false)
  useEffectWithCleanup {
    isMounted.current = true
    return@useEffectWithCleanup { isMounted.current = false }
  }
  return isMounted
}
