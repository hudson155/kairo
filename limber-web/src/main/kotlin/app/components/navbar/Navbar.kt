package app.components.navbar

import app.components.navbar.components.headerLink.headerLink
import app.components.navbar.components.headerLinkGroup.headerLinkGroup
import app.components.navbar.components.headerText.headerText
import app.default
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import kotlinx.css.Color
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
import styled.css
import styled.styledDiv

internal data class Props(val features: List<FeatureRep.Complete>, val name: String?) : RProps

private val navbar = functionalComponent<Props> { props ->
    styledDiv {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            height = 32.px
            backgroundColor = Color("#24292E")
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
                headerLink(to = "/signout") { +"Sign Out" }
            }
        }
    }
}

internal fun RBuilder.navbar(features: List<FeatureRep.Complete>, name: String?) {
    child(navbar, Props(features, name))
}
