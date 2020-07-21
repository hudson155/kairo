package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.components.memberRow.memberRow
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.orgRoleMembersSelector
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * A single row in an [orgRoleMembersSelector]. Shows the profile photo and name. Allows removing the member.
 *
 * [user] is the member to represent.
 *
 * [onRemove] is called to remove the user from the org role.
 *
 * [children] should only be used for attributes.
 */
internal fun RBuilder.orgRoleMembersSelectorMember(
  user: UserRep.Summary,
  onRemove: () -> Unit,
  children: RHandler<Props>
) {
  child(component, Props(user, onRemove), handler = children)
}

internal data class Props(val user: UserRep.Summary, val onRemove: () -> Unit) : RProps

private class S : Styles("OrgRoleMembersSelectorMember") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
    justifyContent = JustifyContent.spaceBetween
    height = 48.px
    padding(12.px)
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
  }
  val containerRed by css {
    backgroundColor = Color.red.withAlpha(0.1)
  }
  val right by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
  }
  val button by css {
    marginRight = 12.px
    lastOfType {
      marginRight = 0.px
    }
  }
}

private val s = S().apply { inject() }

/**
 * The default state is [State.DEFAULT]. When the remove button is clicked, the state changes to [State.REMOVING], which
 * gives the user a chance to cancel or confirm. If they confirm, the state changes to [State.REMOVE_SAVING] which shows
 * a spinner.
 */
private enum class State { DEFAULT, REMOVING, REMOVE_SAVING }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (state, setState) = useState(State.DEFAULT)

  div(
    classes = cls(
      s.c { it::container },
      s.c(state in setOf(State.REMOVING, State.REMOVE_SAVING)) { it::containerRed }
    )
  ) {
    div {
      memberRow(props.user, grayscale = state in setOf(State.REMOVING, State.REMOVE_SAVING))
    }
    div(classes = s.c { it::right }) {
      when (state) {
        State.DEFAULT -> {
          limberButton(
            style = Style.DANGER,
            onClick = { setState(State.REMOVING) },
            classes = s.c { it::button }
          ) { +"Remove" }
        }
        State.REMOVING -> {
          limberButton(
            style = Style.DANGER,
            onClick = {
              setState(State.REMOVE_SAVING)
              props.onRemove()
            },
            classes = s.c { it::button }
          ) { +"Confirm" }
          limberButton(
            style = Style.SECONDARY,
            onClick = { setState(State.DEFAULT) },
            classes = s.c { it::button }
          ) { +"Cancel" }
        }
        State.REMOVE_SAVING -> {
          loadingSpinner()
        }
      }
    }
  }
  props.children()
}
