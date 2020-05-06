package io.limberapp.web.util

import react.RBuilder
import react.ReactElement

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal fun buildElements(handler: RBuilder.() -> Unit) = react.buildElements(handler) as ReactElement?
