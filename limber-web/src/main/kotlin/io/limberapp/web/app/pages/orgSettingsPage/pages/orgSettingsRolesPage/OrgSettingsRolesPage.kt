package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.orgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.util.Page
import io.limberapp.web.util.Strings
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolesPage() {
    child(component)
}

internal val orgSettingsRolesPage = Page(
    name = "Roles & permissions",
    path = "${orgSettingsPage.path}/roles"
)

private val component = functionalComponent<RProps> {
    layoutTitle(orgSettingsRolesPage.name, Strings.orgSettingsRolesPageDescription)
    orgRolesTable()
}
