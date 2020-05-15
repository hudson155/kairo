package io.limberapp.web

import io.limberapp.web.app.app
import react.dom.*
import kotlin.browser.document

internal fun main() {
  render(document.getElementById("root")) {
    app()
  }
}
