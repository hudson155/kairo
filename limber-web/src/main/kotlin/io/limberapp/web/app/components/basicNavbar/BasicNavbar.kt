package io.limberapp.web.app.components.basicNavbar

import io.limberapp.web.app.components.navbar.components.headerLinkGroup.headerLinkGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent

private val basicNavbar = functionalComponent<RProps> { props ->
    navbar(
        left = buildElements {
            headerLinkGroup {
                headerItem { +"Limber" }
            }
        },
        right = buildElements {
            headerLinkGroup {
                props.children()
            }
        }
    )
}

internal fun RBuilder.basicNavbar(children: RHandler<RProps> = {}) {
    child(basicNavbar, handler = children)
}
