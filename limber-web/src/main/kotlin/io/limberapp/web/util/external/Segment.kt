package io.limberapp.web.util.external

import org.w3c.dom.Window

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal val Window.segment: Segment
  get() = this.asDynamic().analytics as Segment

internal external interface Segment {
  fun load(writeKey: String)
  fun page()
}
