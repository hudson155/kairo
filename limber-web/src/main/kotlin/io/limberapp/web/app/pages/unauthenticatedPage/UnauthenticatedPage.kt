package io.limberapp.web.app.pages.unauthenticatedPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.util.Strings
import io.limberapp.web.util.Styles
import kotlinx.css.TextAlign
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.textDecoration
import kotlinx.css.textAlign
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.dom.h1
import react.dom.span
import react.functionalComponent
import react.router.dom.navLink
import styled.getClassName

/**
 * The only page shown when in an unauthenticated state.
 */
internal fun RBuilder.unauthenticatedPage() {
    child(component)
}

private val styles = object : Styles("UnauthenticatedPage") {
    val container by css {
        textAlign = TextAlign.center
    }
    val signInLink by css {
        textDecoration(TextDecorationLine.underline)
    }
}.apply { inject() }

private val component = functionalComponent<RProps> {
    centeredContentLayout {
        div(classes = styles.getClassName { it::container }) {
            h1 { +Strings.welcomeTitle }
            navLink<RProps>(to = SignInPage.path, exact = true) {
                span(classes = styles.getClassName { it::signInLink }) { +Strings.clickHereToSignIn }
            }
        }
    }
}
