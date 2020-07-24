package io.limberapp.web.app.root.appRootRouter

import io.limberapp.web.app.components.minimalPage.LinkType
import io.limberapp.web.app.components.minimalPage.minimalPage
import io.limberapp.web.app.components.pageTracker.pageTracker
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.SignOutPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.app.pages.unauthenticatedPage.unauthenticatedPage
import io.limberapp.web.app.root.appFeatureRouter.appFeatureRouter
import io.limberapp.web.app.root.globalStateProvider.globalStateProvider
import io.limberapp.web.auth.useAuth
import io.limberapp.web.util.rootPath
import react.*
import react.router.dom.*

/**
 * Handles routing for the unauthenticated portion of the application.
 */
internal fun RBuilder.appRootRouter() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val auth = useAuth()

  // While auth is loading, show the loading page.
  if (auth.isLoading) return minimalPage(linkType = null) { loadingPage("Identifying you...") }

  pageTracker()

  switch {
    route(path = SignInPage.path, exact = true) { buildElement { signInPage() } }
    route(path = SignOutPage.path, exact = true) { buildElement { signOutPage() } }
    if (auth.isAuthenticated) {
      route(path = rootPath) { buildElement { globalStateProvider { appFeatureRouter() } } }
    } else {
      route(path = rootPath) { buildElement { minimalPage(linkType = LinkType.SIGN_IN) { unauthenticatedPage() } } }
    }
  }
}
