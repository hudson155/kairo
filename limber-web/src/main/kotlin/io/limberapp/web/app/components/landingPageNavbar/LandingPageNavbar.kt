package io.limberapp.web.app.components.landingPageNavbar

import io.limberapp.web.app.components.navbar.components.headerLinkGroup.headerLinkGroup
import io.limberapp.web.app.components.navbar.components.headerText.headerText
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

private val landingPageNavbar = functionalComponent<RProps> { props ->
    navbar(
        left = buildElements {
            headerLinkGroup {
                headerText { +"Limber" }
            }
        },
        right = null
    )
}

internal fun RBuilder.landingPageNavbar() {
    child(landingPageNavbar)
}
