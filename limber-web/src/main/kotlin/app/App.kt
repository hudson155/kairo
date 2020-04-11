package app

import app.pages.signInPage.signInPage
import app.pages.signOutPage.signOutPage
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch

private val app = functionalComponent<RProps> {
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
