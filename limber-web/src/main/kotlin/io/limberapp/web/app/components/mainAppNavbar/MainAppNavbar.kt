package io.limberapp.web.app.components.mainAppNavbar

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.mainAppNavbar.components.mainAppNavbarUserDropdown.mainAppNavbarUserDropdown
import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.components.headerPhoto.headerPhoto
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.default
import io.limberapp.web.util.buildElements
import kotlinx.css.Align
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink
import react.useState
import styled.css
import styled.styledDiv

internal data class Props(val features: Set<FeatureRep.Complete>, val name: String, val photoUrl: String?) : RProps

private enum class OpenItem { USER_DROPDOWN }

private val mainAppNavbar = functionalComponent<Props> { props ->
    val (openItem, setOpenItem) = useState<OpenItem?>(null)
    val toggleOpenItem: (OpenItem) -> Unit = { setOpenItem(if (openItem == null) it else null) }

    navbar(
        left = buildElements {
            headerGroup {
                props.features.default?.let {
                    navLink<RProps>(to = it.path) {
                        headerItem { +"Limber" }
                    }
                }
            }
        },
        right = buildElements {
            headerGroup {
                styledDiv {
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        alignItems = Align.center
                        if (openItem == null) cursor = Cursor.pointer
                    }
                    attrs {
                        onClickFunction = { toggleOpenItem(OpenItem.USER_DROPDOWN) }
                    }
                    headerItem { +props.name }
                    props.photoUrl?.let { headerPhoto(it) }
                }
                if (openItem == OpenItem.USER_DROPDOWN) {
                    mainAppNavbarUserDropdown(props.name)
                }
            }
        }
    ) {
        headerGroup {
            props.features.forEach {
                navLink<RProps>(to = it.path) {
                    headerItem { +it.name }
                }
            }
        }
    }
}

internal fun RBuilder.mainAppNavbar(features: Set<FeatureRep.Complete>, name: String, photoUrl: String?) {
    child(mainAppNavbar, Props(features, name, photoUrl))
}
