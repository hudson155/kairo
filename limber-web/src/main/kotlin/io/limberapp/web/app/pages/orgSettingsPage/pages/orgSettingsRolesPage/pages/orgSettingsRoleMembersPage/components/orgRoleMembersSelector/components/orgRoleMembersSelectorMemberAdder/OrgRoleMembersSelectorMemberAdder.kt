package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMemberAdder

import com.piperframework.types.UUID
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.components.orgMemberSelector.orgMemberSelector
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import react.*
import react.dom.*

internal fun RBuilder.orgRoleMembersSelectorMemberAdder(
  excludedUserGuids: Set<UUID> = emptySet(),
  onAdd: (accountGuid: UUID) -> Unit
) {
  child(component, Props(excludedUserGuids, onAdd))
}

internal data class Props(val excludedUserGuids: Set<UUID>, val onAdd: (accountGuid: UUID) -> Unit) : RProps

private class S : Styles("OrgRoleMembersSelectorMemberAdder") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
    justifyContent = JustifyContent.spaceBetween
    height = 48.px
    padding(12.px)
  }
  val containerBlue by css {
    backgroundColor = Color.blue.withAlpha(0.1)
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
 * The default state is [State.DEFAULT]. When the add button is clicked, the state changes to [State.ADDING], which
 * shows the [orgMemberSelector]. When they add a member, the state changes to [State.ADD_SAVING] which shows a spinner.
 */
private enum class State { DEFAULT, ADDING, ADD_SAVING }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (selectedUserGuid, setSelectedUserGuid) = useState<UUID?>(null)
  val (state, setState) = useState(State.DEFAULT)

  div(
    classes = cls(
      s.c { it::container },
      s.c(state in setOf(State.ADDING, State.ADD_SAVING)) { it::containerBlue }
    )
  ) {
    div {
      if (state in setOf(State.ADDING, State.ADD_SAVING)) {
        orgMemberSelector(excludedUserGuids = props.excludedUserGuids, onSelect = setSelectedUserGuid)
      }
    }
    div(classes = s.c { it::right }) {
      when (state) {
        State.DEFAULT -> {
          limberButton(
            style = Style.SECONDARY,
            onClick = { setState(State.ADDING) },
            classes = s.c { it::button }
          ) { inlineIcon("plus") }
        }
        State.ADDING -> {
          if (selectedUserGuid != null) {
            limberButton(
              style = Style.PRIMARY,
              onClick = {
                setState(State.ADD_SAVING)
                props.onAdd(selectedUserGuid)
              },
              classes = s.c { it::button }
            ) { +"Confirm" }
          }
          limberButton(
            style = Style.SECONDARY,
            onClick = { setState(State.DEFAULT) },
            classes = s.c { it::button }
          ) { +"Cancel" }
        }
        State.ADD_SAVING -> {
          loadingSpinner()
        }
      }
    }
  }
}
