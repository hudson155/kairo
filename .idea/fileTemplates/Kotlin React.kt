#set($FUNCTION_NAME = $NAME.substring(0,1).toLowerCase() + $NAME.substring(1))

#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.state.state.formInstances.useFormInstancesState
import io.limberapp.web.util.Styles
import react.*
import react.router.dom.*

internal fun RBuilder.${FUNCTION_NAME}(
  // Update Props
) {
  child(component, Props())
}

internal data class Props(
  // Update props
) : RProps

private class S : Styles("${NAME}") {
  // All the fantastic CSS'ing you need 
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  // General Hooks
  val history = useHistory()

  // Limber State
  val (formInstances, formInstancesMutator) = useFormInstancesState()

  // Any custom destructuring your heart desires
  val formInstanceGuids = formInstances.values.map { it.guid }

  // Custom state
  val (buttonStyle, setButtonStyle) = useState(Style.PRIMARY)
  val (clicks, setClicks) = useState(0)

  // Effects
  useEffect(listOf(clicks)) {
    val newStyle = if (clicks % 2 == 0) Style.PRIMARY else Style.DANGER // we cant trust odd numbers...
    setButtonStyle(newStyle)
  }

  // Custom lambdas
  val incrementClicks = {
    setClicks(clicks + 1)
  }

  // Error handling
  if (clicks < 0) return failedToLoad("Well, we didn't fail to load... but you get the idea")

  // Rendering
  if (formInstances == null) return loadingSpinner()
  limberButton(
    style = buttonStyle,
    onClick = incrementClicks
  ) { +"Click me!" }
}
