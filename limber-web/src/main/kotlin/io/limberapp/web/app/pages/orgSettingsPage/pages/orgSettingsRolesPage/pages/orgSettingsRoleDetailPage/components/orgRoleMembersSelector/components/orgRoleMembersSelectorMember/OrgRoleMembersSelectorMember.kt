package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.profilePhoto.profilePhoto
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
import io.limberapp.web.util.initials
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

internal fun RBuilder.orgRoleMembersSelectorMember(
  user: UserRep.Summary?,
  onRemove: (() -> Unit)?,
  children: RHandler<Props> = {}
) {
  child(component, Props(user, onRemove), handler = children)
}

internal data class Props(val user: UserRep.Summary?, val onRemove: (() -> Unit)?) : RProps {
  init {
    require(user != null && onRemove != null || user == null && onRemove == null)
  }
}

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
  val containerBlue by css {
    backgroundColor = Color.blue.withAlpha(0.1)
  }
  val containerRed by css {
    backgroundColor = Color.red.withAlpha(0.1)
  }
  val left by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
  }
  val right by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
  }
  val input by css {
    flexGrow = 1.0
    fontSize = LinearDimension.initial
  }
  val removeButton by css {
    marginRight = 12.px
    lastOfType {
      marginRight = 0.px
    }
  }
}.apply { inject() }

private enum class State { DEFAULT, ADDING, ADD_SAVING, REMOVING, REMOVE_SAVING }

private val component = functionalComponent<Props> { props ->

  val (state, setState) = useState(State.DEFAULT)

  div(
    classes = cls(
      s.c { it::container },
      when (state) {
        State.DEFAULT -> null
        State.ADDING, State.ADD_SAVING -> s.c { it::containerBlue }
        State.REMOVING, State.REMOVE_SAVING -> s.c { it::containerRed }
      }
    )
  ) {
    div(classes = s.c { it::left }) {
      if (props.user != null) {
        profilePhoto(
          placeholder = props.user.fullName.initials,
          url = props.user.profilePhotoUrl,
          grayscale = state in setOf(State.REMOVING, State.REMOVE_SAVING)
        )
        div { +props.user.fullName }
      }
    }
    div(classes = s.c { it::right }) {
      when (state) {
        State.DEFAULT -> {
          if (props.user != null) {
            button(classes = cls(gs.c { it::redButton }, s.c { it::removeButton })) {
              attrs.onClickFunction = { setState(State.REMOVING) }
              +"Remove"
            }
          } else {
            button(classes = cls(gs.c { it::primaryButton }, s.c { it::removeButton })) {
              attrs.onClickFunction = { setState(State.ADDING) }
              +"Add"
            }
          }
        }
        State.ADDING, State.ADD_SAVING -> Unit
        State.REMOVING -> {
          button(classes = cls(gs.c { it::redButton }, s.c { it::removeButton })) {
            attrs.onClickFunction = {
              setState(State.REMOVE_SAVING)
              checkNotNull(props.onRemove)()
            }
            +"Confirm"
          }
          button(classes = cls(gs.c { it::secondaryButton }, s.c { it::removeButton })) {
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
