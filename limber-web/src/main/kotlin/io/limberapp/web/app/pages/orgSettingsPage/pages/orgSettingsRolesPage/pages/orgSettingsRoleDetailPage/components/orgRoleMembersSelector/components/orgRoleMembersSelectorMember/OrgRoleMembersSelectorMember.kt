package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.classes
import io.limberapp.web.util.globalStyles
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.LinearDimension
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.display
import kotlinx.css.filter
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.marginRight
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RHandler
import react.RProps
import react.dom.button
import react.dom.div
import react.dom.img
import react.functionalComponent
import react.useState
import styled.getClassName

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

private val styles = object : Styles("OrgRoleMembersSelectorMember") {
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
  val img by css {
    marginRight = 24.px
    height = 48.px
    maxWidth = 48.px
    borderRadius = Theme.Sizing.borderRadius
  }
  val imgGrayscale by css {
    filter = "grayscale(100%)"
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
    classes = classes(
      styles.getClassName { it::container },
      when (state) {
          State.DEFAULT -> null
          State.ADDING, State.ADD_SAVING -> styles.getClassName { it::containerBlue }
          State.REMOVING, State.REMOVE_SAVING -> styles.getClassName { it::containerRed }
      }
    )
  ) {
    div(classes = styles.getClassName { it::left }) {
      if (props.user != null) {
        props.user.profilePhotoUrl?.let {
          img(
            src = it,
            classes = classes(
              styles.getClassName { it::img },
              when (state) {
                  State.DEFAULT, State.ADDING, State.ADD_SAVING -> null
                  State.REMOVING, State.REMOVE_SAVING -> styles.getClassName { it::imgGrayscale }
              }
            )
          ) {}
        }
        div { +props.user.fullName }
      }
    }
    div(classes = styles.getClassName { it::right }) {
      when (state) {
          State.DEFAULT -> {
              if (props.user != null) {
                  button(
                          classes = classes(
                                  globalStyles.getClassName { it::redButton },
                                  styles.getClassName { it::removeButton }
                          )
                  ) {
                      attrs.onClickFunction = { setState(State.REMOVING) }
                      +"Remove"
                  }
              } else {
                  button(
                          classes = classes(
                                  globalStyles.getClassName { it::primaryButton },
                                  styles.getClassName { it::removeButton }
                          )
                  ) {
                      attrs.onClickFunction = { setState(State.ADDING) }
                      +"Add"
                  }
              }
          }
          State.ADDING, State.ADD_SAVING -> Unit
          State.REMOVING -> {
              button(
                      classes = classes(
                              globalStyles.getClassName { it::redButton },
                              styles.getClassName { it::removeButton }
                      )
              ) {
                  attrs.onClickFunction = {
                      setState(State.REMOVE_SAVING)
                      checkNotNull(props.onRemove)()
                  }
                  +"Confirm"
              }
              button(
                      classes = classes(
                              globalStyles.getClassName { it::secondaryButton },
                              styles.getClassName { it::removeButton }
                      )
              ) {
                  attrs.onClickFunction = { setState(State.DEFAULT) }
                  +"Cancel"
              }
          }
          State.REMOVE_SAVING -> {
              inlineIcon("spinner", classes = globalStyles.getClassName { it::spinner })
          }
      }
    }
  }
  props.children()
}
