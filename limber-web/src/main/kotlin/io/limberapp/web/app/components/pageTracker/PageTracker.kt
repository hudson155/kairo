package io.limberapp.web.app.components.pageTracker

import io.limberapp.web.util.external.segment
import io.limberapp.web.util.process
import react.*
import react.router.dom.*
import kotlin.browser.window

/**
 * Segment.io page tracker. Listens for route changes and manually tracks pages, because this is an SPA so automatic
 * page tracking doesn't work
 */
internal fun RBuilder.pageTracker() {
  child(component)
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: RProps) {
  val location = useLocation()

  useEffect(emptyList()) {
    process.env.SEGMENT_WRITE_KEY?.let {
      // Only enable Segment if the write key is specified.
      window.segment.load(it)
    }
  }

  useEffect(listOf(location.pathname)) {
    if (process.env.SEGMENT_WRITE_KEY == null) return@useEffect
    window.segment.page()
  }
}
