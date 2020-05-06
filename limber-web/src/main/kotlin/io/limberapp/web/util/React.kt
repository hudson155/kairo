package io.limberapp.web.util

import react.RBuilder
import react.RMutableRef
import react.ReactElement
import react.useEffectWithCleanup
import react.useRef

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
