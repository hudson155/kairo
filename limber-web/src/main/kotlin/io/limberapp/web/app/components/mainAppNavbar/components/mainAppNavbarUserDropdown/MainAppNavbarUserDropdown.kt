package io.limberapp.web.app.components.mainAppNavbar.components.mainAppNavbarUserDropdown

import io.limberapp.web.app.components.mainAppNavbar.components.mainAppNavbarUserDropdown.components.mainAppNavbarUserDropdownGroup.mainAppNavbarUserDropdownGroup
import io.limberapp.web.app.components.mainAppNavbar.components.mainAppNavbarUserDropdown.components.mainAppNavbarUserDropdownItem.mainAppNavbarUserDropdownItem
import io.limberapp.web.context.theme.useTheme
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.LinearDimension
import kotlinx.css.Position
import kotlinx.css.QuotedString
import kotlinx.css.backgroundColor
import kotlinx.css.borderBottomColor
import kotlinx.css.borderRadius
import kotlinx.css.content
import kotlinx.css.display
import kotlinx.css.left
import kotlinx.css.minWidth
import kotlinx.css.padding
import kotlinx.css.position
import kotlinx.css.properties.border
import kotlinx.css.px
import kotlinx.css.right
import kotlinx.css.top
import react.RBuilder
import react.RProps
import react.child
import react.dom.b
import react.functionalComponent
import react.router.dom.navLink
import styled.css
import styled.styledDiv

internal data class Props(val name: String) : RProps

private val mainAppNavbarUserDropdown = functionalComponent<Props> { props ->
    val theme = useTheme()

    styledDiv {
        css {
            position = Position.absolute
            top = 60.px
            right = 16.px
            minWidth = 128.px
            backgroundColor = Color.white
            border(1.px, BorderStyle.solid, theme.borderLight)
            borderRadius = 4.px
            after {
                top = (-14).px
                right = 22.px
                left = LinearDimension.auto
                border(7.px, BorderStyle.solid, Color.transparent)
                borderBottomColor = theme.borderLight
                position = Position.absolute
                display = Display.inlineBlock
                content = QuotedString("")
            }
        }
        mainAppNavbarUserDropdownGroup {
            mainAppNavbarUserDropdownItem {
                +"Signed in as"
                b { +props.name }
            }
        }
        mainAppNavbarUserDropdownGroup {
            navLink<RProps>(to = "/signout", exact = true) { mainAppNavbarUserDropdownItem { +"Sign Out" } }
        }
    }
}

internal fun RBuilder.mainAppNavbarUserDropdown(name: String) {
    child(mainAppNavbarUserDropdown, Props(name))
}
