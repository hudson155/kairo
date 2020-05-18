package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.memberRow.memberRow
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.orgRoleMembersSelector
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * A single row in an [orgRoleMembersSelector]. Shows the profile photo and name. Allows removing the member.
 *
 * [user] is the member to represent.
 * [onRemove] is called to remove the user from the org role.
 */
internal fun RBuilder.orgRoleMembersSelectorMember(
  user: UserRep.Summary,
  onRemove: () -> Unit,
  children: RHandler<Props>
) {
  child(component, Props(user, onRemove), handler = children)
}

internal data class Props(val user: UserRep.Summary, val onRemove: () -> Unit) : RProps

private val s = object : Styles("OrgRoleMembersSelectorMember") {
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
}.apply { inject() }

/**
 * The default state is [State.DEFAULT]. When the remove button is clicked, the state changes to [State.REMOVING], which
 * gives the user a chance to cancel or confirm. If they confirm, the state changes to [State.REMOVE_SAVING] which shows
 * a spinner.
 */
private enum class State { DEFAULT, REMOVING, REMOVE_SAVING }

private val component = functionalComponent<Props> { props ->
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
          button(classes = cls(gs.c { it::redButton }, s.c { it::button })) {
            attrs.onClickFunction = { setState(State.REMOVING) }
            +"Remove"
          }
        }
        State.REMOVING -> {
          button(classes = cls(gs.c { it::redButton }, s.c { it::button })) {
            attrs.onClickFunction = {
              setState(State.REMOVE_SAVING)
              props.onRemove()
            }
            +"Confirm"
          }
          button(classes = cls(gs.c { it::secondaryButton }, s.c { it::button })) {
            attrs.onClickFunction = { setState(State.DEFAULT) }
            +"Cancel"
          }
        }
        State.REMOVE_SAVING -> {
          inlineIcon("spinner", classes = gs.c { it::spinner })
        }
      }
    }
  }
  props.children()
}
