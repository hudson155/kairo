package io.limberapp.web.app.pages.unauthenticatedPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import kotlinx.css.TextAlign
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.textDecoration
import kotlinx.css.textAlign
import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.functionalComponent
import react.router.dom.navLink
import styled.css
import styled.styledDiv
import styled.styledSpan

/**
 * The only page shown when in an unauthenticated state.
 */
internal fun RBuilder.unauthenticatedPage() {
    child(unauthenticatedPage)
}

private val unauthenticatedPage = functionalComponent<RProps> {
    centeredContentLayout {
        styledDiv {
            css { textAlign = TextAlign.center }
            h1 { +"Welcome to Limber" }
            navLink<RProps>(to = "/signin", exact = true) {
                styledSpan {
                    css { textDecoration(TextDecorationLine.underline) }
                    +"Click here to sign in"
                }
            }
        }
    }
}
