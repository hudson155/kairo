package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable

import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.context.theme.useTheme
import io.limberapp.web.util.withContext
import kotlinx.css.BorderStyle
import kotlinx.css.RuleSet
import kotlinx.css.backgroundColor
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.properties.borderBottom
import kotlinx.css.properties.borderTop
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.dom.tbody
import react.dom.thead
import react.dom.tr
import react.functionalComponent
import styled.css
import styled.styledTable
import styled.styledTd
import styled.styledTh
import styled.styledTr

internal fun RBuilder.orgRolesTable() {
    child(orgRolesTable)
}

private val orgRolesTable = functionalComponent<RProps> {
    val api = useApi()
    val global = useGlobalState()
    val theme = useTheme()

    withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

    // While the org roles are loading, show nothing.
    if (!global.state.orgRoles.isLoaded) return@functionalComponent

    val orgRoles = checkNotNull(global.state.orgRoles.state)

    if (orgRoles.isEmpty()) {
        p { +"No roles are defined." }
        return@functionalComponent
    }

    val cellStyle: RuleSet = {
        padding(4.px)
    }

    styledTable {
        css {
            maxWidth = 768.px
        }
        thead {
            tr {
                styledTh {
                    css { +cellStyle }
                    +"Name"
                }
                styledTh {
                    css { +cellStyle }
                    +"Permissions"
                }
                styledTh {
                    css { +cellStyle }
                    +"Members"
                }
            }
        }
        tbody {
            orgRoles.forEach {
                styledTr {
                    css {
                        +cellStyle
                        borderTop(1.px, BorderStyle.solid, theme.borderLight)
                        lastOfType {
                            borderBottom(1.px, BorderStyle.solid, theme.borderLight)
                        }
                        hover {
                            backgroundColor = theme.backgroundLightSubtleAccent
                        }
                    }
                    styledTd {
                        css { +cellStyle }
                        +it.name
                    }
                    styledTd {
                        css { +cellStyle }
                        +"${it.permissions.permissions.size} permissions"
                    }
                    styledTd {
                        css { +cellStyle }
                        +"${it.memberCount} members"
                    }
                }
            }
        }
    }
}
