package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRolePermissionsPage.components.orgRolePermissionsSelector

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.util.Styles
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import io.limberapp.web.util.targetChecked
import io.limberapp.web.util.useIsMounted
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*

internal fun RBuilder.orgRolePermissionsSelector(
  permissions: OrgPermissions,
  onChangePermissions: suspend (OrgPermissions) -> Unit,
  onClose: () -> Unit,
) {
  child(component, Props(permissions, onChangePermissions, onClose))
}

internal data class Props(
  val permissions: OrgPermissions,
  val onChangePermissions: suspend (OrgPermissions) -> Unit,
  val onClose: () -> Unit,
) : RProps

private class S : Styles("OrgRolePermissionsSelector") {
  val rowsContainer by css {
    padding(vertical = 8.px)
  }
  val row by css {
    marginBottom = 8.px
    lastOfType {
      marginBottom = 0.px
    }
  }
  val label by css {
    display = Display.flex
    flexDirection = FlexDirection.row
  }
}

private val s = S().apply { inject() }

/**
 * The default state is [State.DEFAULT]. This represents that the permissions are selectable. It does not suggest that
 * the permission values displayed reflect the permission values actually saved with the org role, nor does it suggest
 * the opposite. When the user saves the permissions, the state will change to [State.SAVING] while they are saved,
 * before reverting to [State.DEFAULT].
 */
private enum class State { DEFAULT, SAVING }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val isMounted = useIsMounted()

  val (state, setState) = useState(State.DEFAULT)
  val (permissions, setPermissions) = useState(props.permissions)

  val setPermissionValue = { permission: OrgPermission, value: Boolean ->
    setPermissions(permissions.withPermission(permission, value))
  }

  val onSave = {
    setState(State.SAVING)
    async {
      props.onChangePermissions(permissions)
      if (isMounted.current) {
        setState(State.DEFAULT)
        props.onClose()
      }
    }
  }

  div(classes = s.c { it::rowsContainer }) {
    OrgPermission.values().sortedBy { it.bit }.forEach { permission ->
      div(classes = s.c { it::row }) {
        label(classes = s.c { it::label }) {
          span {
            input(type = InputType.checkBox) {
              attrs.defaultChecked = permission in props.permissions
              attrs.disabled = state == State.SAVING
              attrs.onChangeFunction = { setPermissionValue(permission, it.targetChecked) }
            }
          }
          span {
            b { +permission.title }
            br { }
            small { +permission.description }
          }
        }
      }
    }
  }
  div(classes = gs.c { it::modalFooter }) {
    limberButton(
      style = Style.PRIMARY,
      loading = state == State.SAVING,
      onClick = onSave
    ) { +"Save" }
    limberButton(
      style = Style.SECONDARY,
      loading = state == State.SAVING,
      onClick = props.onClose
    ) { +"Cancel" }
  }
}

