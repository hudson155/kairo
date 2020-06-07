package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.components.orgRolesTable.components.orgRoleTableRoleDeleter.components.orgRoleTableRoleDeleterModal

import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.component
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A modal to confirm deletion of an org role.
 *
 * [orgRoleName] is the name of the org role.
 */
internal fun RBuilder.orgRolesTableRoleDeleterModal(orgRoleName: String, onDelete: () -> Unit, onCancel: () -> Unit) {
  child(component, Props(orgRoleName, onDelete, onCancel))
}

internal data class Props(val orgRoleName: String, val onDelete: () -> Unit, val onCancel: () -> Unit) : RProps

private class S : Styles("OrgRolesTableRoleDeleterModal") {
  val buttons by css {
    display = Display.flex
    flexDirection = FlexDirection.rowReverse

  }
  val button by css {
    marginLeft = 12.px
    lastOfType {
      marginLeft = 0.px
    }
  }
}

private val s = S().apply { inject() }

/**
 * The default state is [State.DEFAULT], which means it's just displaying the modal. If the user confirms their action,
 * the state will switch to [State.SAVING] and show a spinner until it's saved.
 */
private enum class State { DEFAULT, SAVING }

private val component = component<Props> component@{ props ->
  val (state, setState) = useState(State.DEFAULT)

  modal(narrow = true, onClose = props.onCancel) {
    modalTitle("Confirm deletion", "Are you sure you wish to delete ${props.orgRoleName}?")
    div(classes = s.c { it::buttons }) {
      limberButton(
        style = Style.SECONDARY,
        loading = state == State.SAVING,
        onClick = props.onCancel,
        classes = s.c { it::button }
      ) { +"Cancel" }
      limberButton(
        style = Style.DANGER,
        loading = state == State.SAVING,
        onClick = {
          setState(State.SAVING)
          props.onDelete()
        },
        classes = s.c { it::button }
      ) { +"Delete" }
    }
  }
}
