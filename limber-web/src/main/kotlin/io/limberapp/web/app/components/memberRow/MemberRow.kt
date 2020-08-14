package io.limberapp.web.app.components.memberRow

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.profilePhoto.profilePhoto
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
import io.limberapp.web.util.initials
import io.limberapp.web.util.xs
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * A row that represents a member, generally used as part of a list, such as a list of org members or a list of org role
 * members.
 *
 * If [onSelect] is provided, the row will be clickable and the function will be called when the row is clicked.
 */
internal fun RBuilder.memberRow(
  user: UserRep.Summary,
  small: Boolean = false,
  hideNameXs: Boolean = false,
  grayscale: Boolean = false,
  onSelect: (() -> Unit)? = null,
) {
  child(component, Props(user, small, hideNameXs, grayscale, onSelect))
}

internal data class Props(
  val user: UserRep.Summary,
  val small: Boolean,
  val hideNameXs: Boolean,
  val grayscale: Boolean,
  val onSelect: (() -> Unit)?,
) : RProps

private class S : Styles("MemberRow") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
  }
  val clickable by css {
    cursor = Cursor.pointer
  }
  val profilePhoto by css {
    marginRight = 24.px
  }
  val profilePhotoSmall by css {
    marginRight = 16.px
  }
  val profilePhotoNoMarginXs by css {
    xs {
      marginRight = 0.px
    }
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = cls(s.c { it::container }, s.c(props.onSelect != null) { it::clickable })) {
    props.onSelect?.let { attrs.onClickFunction = { it() } }
    profilePhoto(
      placeholder = props.user.fullName.initials,
      url = props.user.profilePhotoUrl,
      small = props.small,
      grayscale = props.grayscale,
      classes = cls(
        s.c { it::profilePhoto },
        s.c(props.small) { it::profilePhotoSmall },
        s.c(props.hideNameXs) { it::profilePhotoNoMarginXs }
      )
    )
    div(classes = gs.c(props.hideNameXs) { it::hiddenXs }) { +props.user.fullName }
  }
}
