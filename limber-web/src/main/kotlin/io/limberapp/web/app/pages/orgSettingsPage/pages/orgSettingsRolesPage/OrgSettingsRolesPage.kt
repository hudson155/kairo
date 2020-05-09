package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage

import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.OrgSettingsRoleDetailPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.orgSettingsRoleDetailPage
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolesListPage.orgSettingsRolesListPage
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.route
import react.router.dom.switch
import react.router.dom.useRouteMatch

/**
 * Parent page for organization role and organization role membership settings.
 */
internal fun RBuilder.orgSettingsRolesPage() {
    child(component)
}

internal object OrgSettingsRolesPage {
    const val name = "Roles & permissions"
    const val path = "${OrgSettingsPage.path}/roles"
}

private val component = functionalComponent<RProps> {
    val match = checkNotNull(useRouteMatch<RProps>())

    switch {
        route(path = match.path, exact = true) {
            buildElement { orgSettingsRolesListPage() }
        }
        route(path = "${match.path}/:${OrgSettingsRoleDetailPage.PageParams::roleSlug.name}", exact = true) {
            buildElement { orgSettingsRoleDetailPage() }
        }
    }
}
