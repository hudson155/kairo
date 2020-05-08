package io.limberapp.web.app.components.basicNavbar

import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.util.Strings
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent

/**
 * Top-of-page nav for use in an unauthenticated or partially loaded state. [children] should be [headerItem]s for the
 * right section of the nav. There's no way to have a main section to this nav implementation.
 */
internal fun RBuilder.basicNavbar(children: RHandler<RProps> = {}) {
    child(component, handler = children)
}

private val component = functionalComponent<RProps> { props ->
    navbar(
        left = buildElements { headerGroup { headerItem { +Strings.limber } } },
        right = buildElements { headerGroup { props.children() } }
    ) {}
}
