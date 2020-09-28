package io.limberapp.web.app.pages.featurePage.pages.setNamePage

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.state.state.tenant.useTenantState
import io.limberapp.web.state.state.user.useUserState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.targetValue
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import react.*
import react.dom.*

internal fun RBuilder.setNamePage() {
  child(component)
}

internal typealias Props = RProps

private class S : Styles("SetNamePage") {
  val form by css {
    display = Display.flex
    flexDirection = FlexDirection.column
  }
  val row by css {
    marginBottom = 8.px
  }
  val input by css {
    flexGrow = 1.0
    fontSize = LinearDimension.initial
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (tenant, _) = useTenantState()
  val (user, userMutator) = useUserState()

  val (firstName, setFirstName) = useState(user.firstName ?: "")
  val (lastName, setLastName) = useState(user.lastName ?: "")

  val submit = {
    async {
      userMutator.patch(user.guid, UserRep.Update(firstName = firstName, lastName = lastName))
    }
  }

  standardLayout {
    layoutTitle("Welcome to ${tenant.name}!")
    p { +"Before you can get started, we just need to know your name." }
    form(classes = s.c { it::form }) {
      attrs.onSubmitFunction = { e ->
        e.preventDefault()
        submit()
      }
      label(classes = s.c { it::row }) {
        +"First name: "
        input(type = InputType.text, classes = s.c { it::input }) {
          attrs.autoFocus = firstName.isEmpty()
          attrs.value = firstName
          attrs.onChangeFunction = { setFirstName(it.targetValue) }
        }
      }
      label(classes = s.c { it::row }) {
        +"Last name: "
        input(type = InputType.text, classes = s.c { it::input }) {
          attrs.autoFocus = firstName.isNotEmpty()
          attrs.value = lastName
          attrs.onChangeFunction = { setLastName(it.targetValue) }
        }
      }
      limberButton(style = Style.PRIMARY, onClick = submit) {
        +"Submit"
      }
    }
  }
}
