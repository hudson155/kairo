package io.limberapp.web.app.components.mainAppNavbar.components.mainAppNavbarUserDropdown.components.mainAppNavbarUserDropdownItem

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

private val mainAppNavbarUserDropdownItem = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.flexStart
        }
        props.children()
    }
}

internal fun RBuilder.mainAppNavbarUserDropdownItem(children: RHandler<RProps>) {
    child(mainAppNavbarUserDropdownItem, handler = children)
}
