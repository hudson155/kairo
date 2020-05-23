package io.limberapp.web.hook

import io.limberapp.web.util.EventType
import io.limberapp.web.util.KeyCode
import io.limberapp.web.util.keyCode
import org.w3c.dom.events.Event
import react.*
import kotlin.browser.document

/**
 * Listens for the escape key to be pressed.
 *
 * [callback] is called when it occurs.
 */
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
