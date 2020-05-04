package io.limberapp.web.app.components.basicNavbar

import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent

/**
 * Top-of-page nav for use in an unauthenticated or partially loaded state.
 */
internal fun RBuilder.basicNavbar(children: RHandler<RProps> = {}) {
    child(basicNavbar, handler = children)
}

private val basicNavbar = functionalComponent<RProps> { props ->
    navbar(
        left = buildElements { headerGroup { headerItem { +"Limber" } } },
        right = buildElements { headerGroup { props.children() } }
    ) {}
}
