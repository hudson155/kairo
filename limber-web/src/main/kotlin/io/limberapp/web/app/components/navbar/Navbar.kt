package io.limberapp.web.app.components.navbar

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.navbar.components.headerLink.headerLink
import io.limberapp.web.app.components.navbar.components.headerLinkGroup.headerLinkGroup
import io.limberapp.web.app.components.navbar.components.headerText.headerText
import io.limberapp.web.app.default
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.context.theme.ThemeContext
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.useContext
import styled.css
import styled.styledDiv

internal data class Props(val features: List<FeatureRep.Complete>, val name: String?) : RProps

private val navbar = functionalComponent<Props> { props ->
    val auth = useAuth()
    val theme = useContext(ThemeContext)

    styledDiv {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            height = 32.px
            backgroundColor = theme.backgroundDark
            padding(vertical = 16.px, horizontal = 0.px)
        }
        styledDiv {
            css { display = Display.flex }
            headerLinkGroup {
                props.features.default?.let { headerLink(to = it.path) { +"Limber" } } ?: headerText { +"Limber" }
            }
            headerLinkGroup {
                props.features.forEach { headerLink(to = it.path) { +it.name } }
            }
        }
        styledDiv {
            css { display = Display.flex }
            headerLinkGroup {
                props.name?.let { headerText { +it } }
                if (auth.isAuthenticated) {
                    headerLink(to = "/signout") { +"Sign Out" }
                } else {
                    headerLink(to = "/signin") { +"Sign In" }
                }
            }
        }
    }
}

internal fun RBuilder.navbar(features: List<FeatureRep.Complete>, name: String?) {
    child(navbar, Props(features, name))
}
