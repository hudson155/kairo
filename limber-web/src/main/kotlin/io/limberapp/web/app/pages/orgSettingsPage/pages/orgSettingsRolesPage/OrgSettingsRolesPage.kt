package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
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

internal object OrgSettingsRolesPage {
    const val name = "Roles & permissions"
    const val path = "${OrgSettingsPage.path}/roles"
}

private val component = functionalComponent<RProps> {
    layoutTitle(OrgSettingsRolesPage.name, Strings.orgSettingsRolesPageDescription)
    orgRolesTable()
}
