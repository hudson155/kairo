package io.limberapp.web.hook

import io.limberapp.web.util.EventType
import io.limberapp.web.util.KeyCode
import io.limberapp.web.util.keyCode
import org.w3c.dom.events.Event
import react.RDependenciesList
import react.useEffectWithCleanup
import kotlin.browser.document

internal fun useEscapeKeyListener(dependencies: RDependenciesList, callback: (Event) -> Unit) {
  val onEscape = { event: Event ->
    if (event.keyCode == KeyCode.escape) {
      callback(event)
    }
  }

  useEffectWithCleanup(dependencies) {
    document.addEventListener(EventType.keydown, onEscape)
    return@useEffectWithCleanup { document.removeEventListener(EventType.keydown, onEscape) }
  }
}
