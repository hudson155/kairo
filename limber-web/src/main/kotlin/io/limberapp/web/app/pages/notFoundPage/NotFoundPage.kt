package io.limberapp.web.app.pages.notFoundPage

import io.limberapp.web.app.pages.errorPage.errorPage
import io.limberapp.web.util.Page
import react.*

internal fun RBuilder.notFoundPage() {
  errorPage(NotFoundPage.name, "We looked everywhere, but we couldn't find the page you were looking for.")
}

internal object NotFoundPage : Page {
  const val name = "Not Found"
}
