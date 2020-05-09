package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleEditModal.orgRoleEditModal
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.orgRolesTable
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Strings
import io.limberapp.web.util.withContext
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.redirect
import react.router.dom.useRouteMatch

/**
 * Page for managing organization roles and organization role memberships.
 */
internal fun RBuilder.orgSettingsRolesPage() {
    child(component)
}

internal data class PageParams(val roleName: String) : RProps

internal object OrgSettingsRolesPage {
    const val name = "Roles & permissions"
    const val path = "${OrgSettingsPage.path}/roles"
}

private val component = functionalComponent<RProps> {
    val api = useApi()
    val global = useGlobalState()
    val match = useRouteMatch<PageParams>("${OrgSettingsRolesPage.path}/:${PageParams::roleName.name}")

    withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

    layoutTitle(OrgSettingsRolesPage.name, Strings.orgSettingsRolesPageDescription)

    // While the org roles are loading, show nothing else.
    if (!global.state.orgRoles.isLoaded) return@functionalComponent

    val orgRoles = checkNotNull(global.state.orgRoles.state).values.toSet()

    match?.let {
        val roleName = it.params.roleName
        val orgRole = orgRoles.singleOrNull { it.name == roleName }
        if (orgRole == null) {
            redirect(to = OrgSettingsRolesPage.path)
            return@functionalComponent
        }
        orgRoleEditModal(orgRole)
    }
    orgRolesTable(orgRoles)
}
