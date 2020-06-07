package io.limberapp.web.app.components.pageTracker

import io.limberapp.web.util.component
import io.limberapp.web.util.external.segment
import react.*
import react.router.dom.*
import kotlin.browser.window

/**
 * Segment.io page tracker.
 */
internal fun RBuilder.pageTracker() {
  child(component)
}

private val component = component<RProps> component@{
  val location = useLocation()

  useEffect(listOf(location.pathname)) {
    window.segment.page()
  }
}
