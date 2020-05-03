package io.limberapp.web.app.components.mainAppNavbar.components.mainAppNavbarUserDropdown.components.mainAppNavbarUserDropdownGroup

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.borderBottomStyle
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.padding
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

private val mainAppNavbarUserDropdownGroup = functionalComponent<RProps> { props ->
    val theme = useTheme()

    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            borderBottom(1.px, BorderStyle.solid, theme.borderLight)
            lastOfType {
                borderBottomStyle = BorderStyle.none
            }
            padding(8.px)
        }
        props.children()
    }
}

internal fun RBuilder.mainAppNavbarUserDropdownGroup(children: RHandler<RProps>) {
    child(mainAppNavbarUserDropdownGroup, handler = children)
}
