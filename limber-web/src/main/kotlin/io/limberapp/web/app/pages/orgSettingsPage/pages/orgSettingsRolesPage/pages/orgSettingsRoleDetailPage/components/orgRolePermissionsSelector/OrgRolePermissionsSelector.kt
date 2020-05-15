package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRolePermissionsSelector

import io.limberapp.backend.authorization.permissions.OrgPermission
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRole.OrgRoleAction
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.async
import io.limberapp.web.util.classes
import io.limberapp.web.util.globalStyles
import io.limberapp.web.util.targetChecked
import io.limberapp.web.util.useIsMounted
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.marginRight
import kotlinx.css.padding
import kotlinx.css.paddingTop
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.b
import react.dom.br
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label
import react.dom.small
import react.dom.span
import react.functionalComponent
import react.useState
import styled.getClassName

/**
 * Selector for choosing permissions from a list for an org role.
 */
internal fun RBuilder.orgRolePermissionsSelector(orgRole: OrgRoleRep.Complete, onClose: () -> Unit) {
  child(component, Props(orgRole, onClose))
}

internal data class Props(val orgRole: OrgRoleRep.Complete, val onClose: () -> Unit) : RProps

private val styles = object : Styles("OrgRolePermissionsSelector") {
  val rowsContainer by css {
    padding(vertical = 8.px)
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
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
  val saveButtonContainer by css {
    display = Display.flex
    flexDirection = FlexDirection.rowReverse
    paddingTop = 8.px
  }
  val saveButton by css {
    marginRight = 12.px
  }
}.apply { inject() }

private enum class State { DEFAULT, SAVING }

private val component = functionalComponent<Props> { props ->
  val api = useApi()
  val global = useGlobalState()
  val isMounted = useIsMounted()

    val (state, setState) = useState(State.DEFAULT)
    val (permissions, setPermissions) = useState(props.orgRole.permissions)

  val orgGuid = checkNotNull(global.state.org.state).guid

  val setPermissionValue = { permission: OrgPermission, value: Boolean ->
    setPermissions(permissions.withPermission(permission, value))
  }

  val onSave = { _: Event ->
    setState(State.SAVING)
    async {
      val orgRole = api.orgRoles(
        endpoint = OrgRoleApi.Patch(
          orgGuid = orgGuid,
          orgRoleGuid = props.orgRole.guid,
          rep = OrgRoleRep.Update(permissions = permissions)
        )
      )
      global.dispatch(OrgRoleAction.UpdateValue(orgRole))
      if (isMounted.current) {
        setState(State.DEFAULT)
        props.onClose()
      }
    }
  }

  div(classes = styles.getClassName { it::rowsContainer }) {
    OrgPermission.values().sortedBy { it.bit }.forEach { permission ->
      div(classes = styles.getClassName { it::row }) {
        label(classes = styles.getClassName { it::label }) {
          span {
            input(type = InputType.checkBox) {
              attrs.defaultChecked = props.orgRole.permissions.hasPermission(permission)
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
  div(classes = styles.getClassName { it::saveButtonContainer }) {
    button(
      classes = classes(
        globalStyles.getClassName { it::primaryButton },
        styles.getClassName { it::saveButton }
      )
    ) {
      +"Save"
      attrs.disabled = state == State.SAVING
      attrs.onClickFunction = onSave
    }
    button(
      classes = classes(
        globalStyles.getClassName { it::secondaryButton },
        styles.getClassName { it::saveButton }
      )
    ) {
      +"Cancel"
      attrs.disabled = state == State.SAVING
      attrs.onClickFunction = { props.onClose() }
    }
  }
}

