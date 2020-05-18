package io.limberapp.web.app.components.memberRow

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.profilePhoto.profilePhoto
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.initials
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * A row that represents a member, generally used as part of a list, such as a list of org members or a list of org role
 * members.
 *
 * The [user] is the user to represent.
 * [small] indicates that the size of the row should be on the smaller side. If false, it will be on the larger side.
 * [grayscale] makes the photo grey, representing a disabled state.
 * If [onSelect] is provided, the row will be clickable and the function will be called when the row is clicked.
 */
internal fun RBuilder.memberRow(
  user: UserRep.Summary,
  small: Boolean = false,
  grayscale: Boolean = false,
  onSelect: (() -> Unit)? = null
) {
  child(component, Props(user, small, grayscale, onSelect))
}

internal data class Props(
  val user: UserRep.Summary,
  val small: Boolean,
  val grayscale: Boolean,
  val onSelect: (() -> Unit)?
) : RProps

private val s = object : Styles("MemberRow") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
  }
  val clickable by css {
    cursor = Cursor.pointer
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = cls(s.c { it::container }, s.c(props.onSelect != null) { it::clickable })) {
    props.onSelect?.let { attrs.onClickFunction = { it() } }
    profilePhoto(
      placeholder = props.user.fullName.initials,
      url = props.user.profilePhotoUrl,
      small = props.small,
      grayscale = props.grayscale
    )
    div { +props.user.fullName }
  }
}
