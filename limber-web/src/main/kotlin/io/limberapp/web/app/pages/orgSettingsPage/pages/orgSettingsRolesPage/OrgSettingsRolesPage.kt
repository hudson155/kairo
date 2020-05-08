package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.util.Strings
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.useParams

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolesPage() {
    child(component)
}

internal data class PageParams(val roleName: String?) : RProps

internal object OrgSettingsRolesPage {
    const val name = "Roles & permissions"
    private val roleName = PageParams::roleName.name
    const val path = "${OrgSettingsPage.path}/roles"
    val pathWithParam = "$path/:$roleName"
}

private val component = functionalComponent<RProps> {
    val params = useParams<PageParams>()
    layoutTitle(OrgSettingsRolesPage.name, Strings.orgSettingsRolesPageDescription)
    orgRolesTable(params?.roleName)
}
