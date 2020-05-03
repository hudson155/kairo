package io.limberapp.web.app

import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.app.pages.unauthenticatedPage.unauthenticatedPage
import io.limberapp.web.context.auth.useAuth
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch

private val appRouter = functionalComponent<RProps> {
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
            route(path = "/signin", exact = true) { buildElement { signInPage() } }
            route(path = "/signout", exact = true) { buildElement { signOutPage() } }
            if (auth.isAuthenticated) {
                route(path = "/") { buildElement { appFeatureRouter() } }
            } else {
                route(path = "/") {
                    buildElement {
                        page(
                            header = buildElement {
                                basicNavbar {
                                    navLink<RProps>(to = "/signin", exact = true) { headerItem { +"Sign In" } }
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

internal fun RBuilder.appRouter() {
    child(appRouter)
}
