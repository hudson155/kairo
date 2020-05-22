package io.limberapp.web.hook

import io.limberapp.web.util.EventType
import org.w3c.dom.events.Event
import react.*
import kotlin.browser.document

internal fun useClickListener(dependencies: RDependenciesList, callback: (Event) -> Unit) {
  val handleClick = { event: Event -> callback(event) }
  useEffectWithCleanup(dependencies) {
    document.addEventListener(EventType.click, handleClick)
    return@useEffectWithCleanup { document.removeEventListener(EventType.click, handleClick) }
  }
}
