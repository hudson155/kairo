package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow

import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.backgroundColor
import kotlinx.css.properties.borderBottom
import kotlinx.css.properties.borderTop
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledTr

internal fun RBuilder.orgRolesTableRow(children: RHandler<RProps>) {
    child(orgRolesTableRow, handler = children)
}

private val orgRolesTableRow = functionalComponent<RProps> { props ->
    styledTr {
        css {
            borderTop(1.px, BorderStyle.solid, Theme.borderLight)
            lastOfType {
                borderBottom(1.px, BorderStyle.solid, Theme.borderLight)
            }
            hover {
                backgroundColor = Theme.backgroundLightSubtleAccent
            }
        }
        props.children()
    }
}
