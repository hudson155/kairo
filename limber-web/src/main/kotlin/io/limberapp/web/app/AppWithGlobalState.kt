package io.limberapp.web.app

import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.context.auth0.useAuth
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch

private val appWithGlobalState = functionalComponent<RProps> {
    val auth = useAuth()
    if (auth.isLoading) return@functionalComponent
    browserRouter {
        switch {
            route(path = "/signin", exact = true) { buildElement { signInPage() } }
            route(path = "/signout", exact = true) { buildElement { signOutPage() } }
            route(path = "/") { buildElement { mainApp() } }
        }
    }
}

internal fun RBuilder.appWithGlobalState() {
    child(appWithGlobalState)
}
