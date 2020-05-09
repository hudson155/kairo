package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage

import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.OrgSettingsRolesPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleEditModal.orgRoleEditModal
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.ensureOrgRolesLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.withContext
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.redirect
import react.router.dom.useRouteMatch

/**
 * Page for managing a single organization role and its memberships.
 */
internal fun RBuilder.orgSettingsRoleDetailPage() {
    child(component)
}

internal object OrgSettingsRoleDetailPage {
    internal data class PageParams(val roleSlug: String) : RProps
}

private val component = functionalComponent<RProps> {
    val api = useApi()
    val global = useGlobalState()
    val match = checkNotNull(useRouteMatch<OrgSettingsRoleDetailPage.PageParams>())

    withContext(global, api) { ensureOrgRolesLoaded(checkNotNull(global.state.org.state).guid) }

    orgSettingsRolesListPage()

    // While the org roles are loading, show a blank modal.
    if (!global.state.orgRoles.isLoaded) {
        modal(blank = true) {}
        return@functionalComponent
    }

    val orgRoles = checkNotNull(global.state.orgRoles.state).values.toSet()

    val roleSlug = match.params.roleSlug
    val orgRole = orgRoles.singleOrNull { it.slug == roleSlug }
    if (orgRole == null) {
        redirect(to = OrgSettingsRolesPage.path)
        return@functionalComponent
    }
    orgRoleEditModal(orgRole)
}
