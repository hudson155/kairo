package io.limberapp.web.app

import io.limberapp.web.app.components.minimalPage.LinkType
import io.limberapp.web.app.components.minimalPage.minimalPage
import io.limberapp.web.app.components.pageTracker.pageTracker
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.SignOutPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.app.pages.unauthenticatedPage.unauthenticatedPage
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.component
import io.limberapp.web.util.rootPath
import react.*
import react.router.dom.*

/**
 * Part of the application root. Handles routing for the unauthenticated application.
 */
internal fun RBuilder.appRootRouter() {
  child(component)
}

private val component = component<RProps> component@{
  val auth = useAuth()

  // While auth is loading, show the loading page.
  if (auth.isLoading) return@component minimalPage(linkType = null) { loadingPage("Identifying you...") }

  pageTracker()

  switch {
    route(path = SignInPage.path, exact = true) { buildElement { signInPage() } }
    route(path = SignOutPage.path, exact = true) { buildElement { signOutPage() } }
    if (auth.isAuthenticated) {
      route(path = rootPath) { buildElement { appFeatureRouter() } }
    } else {
      route(path = rootPath) { buildElement { minimalPage(linkType = LinkType.SIGN_IN) { unauthenticatedPage() } } }
    }
  }
}
