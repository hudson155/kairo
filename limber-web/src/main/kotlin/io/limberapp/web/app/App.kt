package io.limberapp.web.app

import io.limberapp.web.api.tenant.getTenant
import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch

private val app = functionalComponent<RProps> {
    val mainScope = MainScope()
    mainScope.launch {
        val videos = getTenant("foo")
    }
    browserRouter {
        switch {
            route(path = "/signin", exact = true) { buildElement { signInPage() } }
            route(path = "/signout", exact = true) { buildElement { signOutPage() } }
            route(path = "/") { buildElement { mainApp() } }
        }
    }
}

internal fun RBuilder.app() {
    child(app)
}
