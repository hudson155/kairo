package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleEditModal

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

internal fun RBuilder.orgRoleEditModal(orgRole: OrgRoleRep.Complete) {
    child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
    modal {
        modalTitle(
            title = "Edit Role: ${props.orgRole.name}",
            description = "Update role metadata, the permissions it grants, and members of the role."
        )
    }
}
