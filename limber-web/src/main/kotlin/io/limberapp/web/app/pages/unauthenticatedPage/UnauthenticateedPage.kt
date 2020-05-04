package io.limberapp.web.app.pages.unauthenticatedPage

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.TextAlign
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexGrow
import kotlinx.css.justifyContent
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
 * The only page shown when in an unauthenticated statee
 */
internal fun RBuilder.unauthenticatedPage() {
    child(unauthenticatedPage)
}

private val unauthenticatedPage = functionalComponent<RProps> {
    styledDiv {
        css {
            flexGrow = 1.0
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.center
        }
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
