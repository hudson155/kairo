package io.limberapp.web.state.state.formTemplates

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.formTemplatesStateProvider(formTemplates: FormTemplatesState, children: RHandler<Props>) {
  child(component, Props(formTemplates), handler = children)
}

internal data class Props(val formTemplates: FormTemplatesState) : RProps

private val formTemplates = createContext<Pair<FormTemplatesState, Unit>>()
internal fun useFormTemplatesState() = useContext(formTemplates)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.formTemplates, Unit))
  child(createElement(formTemplates.Provider, configObject, props.children))
}
