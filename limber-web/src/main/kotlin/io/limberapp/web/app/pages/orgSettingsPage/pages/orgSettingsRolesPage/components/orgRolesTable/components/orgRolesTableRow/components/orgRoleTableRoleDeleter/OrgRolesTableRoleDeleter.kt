package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.components.orgRoleTableRoleDeleter

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRolesTableRow.components.orgRoleTableRoleDeleter.components.orgRoleTableRoleDeleterModal.orgRolesTableRoleDeleterModal
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.OrgRoleAction
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.async
import io.limberapp.web.util.c
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

private val component = functionalComponent<Props> { props ->
  val api = useApi()
  val global = useGlobalState()

  val (state, setState) = useState(State.DEFAULT)

  val orgGuid = checkNotNull(global.state.org.state).guid

  val onDelete = {
    async {
      api.orgRoles(OrgRoleApi.Delete(orgGuid = orgGuid, orgRoleGuid = props.orgRole.guid))
      global.dispatch(OrgRoleAction.DeleteValue(props.orgRole.guid))
    }
  }

  td {
    if (state == State.CONFIRMING) {
      orgRolesTableRoleDeleterModal(props.orgRole.name, onDelete = onDelete, onCancel = { setState(State.DEFAULT) })
    }
    a(classes = s.c { it::icon }) {
      attrs.onClickFunction = { setState(State.CONFIRMING) }
      inlineIcon("trash-alt")
    }
  }
}
