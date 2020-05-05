package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolePage() {
    child(orgSettingsRolePage)
}

internal const val ORG_SETTINGS_ROLE_PAGE_NAME = "Roles & permissions"

private val orgSettingsRolePage = functionalComponent<RProps> {
    layoutTitle(
        title = ORG_SETTINGS_ROLE_PAGE_NAME,
        description = "Roles grant users permissions within your organization."
    )
    orgRolesTable()
}
