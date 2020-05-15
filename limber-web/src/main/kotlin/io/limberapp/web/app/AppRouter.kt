package io.limberapp.web.app

import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.SignOutPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.app.pages.unauthenticatedPage.unauthenticatedPage
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.rootPath
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch

/**
 * Part of the application root.
 *   - Handles auth loading state.
 *   - Handles routing for the unauthenticated application.
 */
internal fun RBuilder.appRouter() {
  child(component)
}

private val component = functionalComponent<RProps> {
  val auth = useAuth()

  // While auth is loading, show the loading page.
  if (auth.isLoading) {
    page(header = buildElement { basicNavbar() }, footer = buildElement { footer() }) {
      loadingPage("Identifying you...")
    }
    return@functionalComponent
  }

  browserRouter {
    switch {
      route(path = SignInPage.path, exact = true) { buildElement { signInPage() } }
      route(path = SignOutPage.path, exact = true) { buildElement { signOutPage() } }
      if (auth.isAuthenticated) {
        route(path = rootPath) { buildElement { appFeatureRouter() } }
      } else {
        route(path = rootPath) {
          buildElement {
            page(
              header = buildElement {
                basicNavbar {
                  navLink<RProps>(
                    to = SignInPage.path,
                    exact = true
                  ) { headerItem { +SignInPage.name } }
                }
              },
              footer = buildElement { footer() }
            ) {
              unauthenticatedPage()
            }
          }
        }
      }
    }
  }
}
