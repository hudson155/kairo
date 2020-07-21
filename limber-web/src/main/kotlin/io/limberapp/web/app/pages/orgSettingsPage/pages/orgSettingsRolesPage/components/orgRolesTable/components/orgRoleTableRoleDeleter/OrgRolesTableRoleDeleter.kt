package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleTableRoleDeleter

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.limberTable.components.limberTableCell.limberTableCell
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleTableRoleDeleter.components.orgRoleTableRoleDeleterModal.orgRolesTableRoleDeleterModal
import io.limberapp.web.context.globalState.action.orgRoles.deleteOrgRole
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.componentWithApi
import io.limberapp.web.util.gs
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * Portion of org roles table that gives the option to delete the org role.
 *
 * [orgRole] is the role to be represented by this component.
 */
internal fun RBuilder.orgRolesTableRoleDeleter(orgRole: OrgRoleRep.Complete) {
  child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private class S : Styles("OrgRolesTableRoleDeleter") {
  val icon by css {
    color = Theme.Color.Text.red
    cursor = Cursor.pointer
  }
}

private val s = S().apply { inject() }

/**
 * The default state is [State.DEFAULT], which means it's just displaying the delete button. If the user clicks the
 * delete button, the state will switch to [State.CONFIRMING], which shows a confirmation modal. If they cancel, it
 * switches back to [State.DEFAULT].
 */
private enum class State { DEFAULT, CONFIRMING }

private val component = componentWithApi(RBuilder::component)
private fun RBuilder.component(self: ComponentWithApi, props: Props) {
  val (state, setState) = useState(State.DEFAULT)

  val onDelete = {
    async { self.deleteOrgRole(props.orgRole.guid) }
  }

  limberTableCell(classes = gs.c { it::hiddenXs }) {
    if (state == State.CONFIRMING) {
      orgRolesTableRoleDeleterModal(props.orgRole.name, onDelete = onDelete, onCancel = { setState(State.DEFAULT) })
    }
    a(classes = s.c { it::icon }) {
      attrs.onClickFunction = { setState(State.CONFIRMING) }
      inlineIcon("trash-alt")
    }
  }
}
