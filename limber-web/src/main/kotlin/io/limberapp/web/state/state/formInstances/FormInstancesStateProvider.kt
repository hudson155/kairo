package io.limberapp.web.state.state.formInstances

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.formInstancesStateProvider(formInstances: FormInstancesState, children: RHandler<Props>) {
  child(component, Props(formInstances), handler = children)
}

internal data class Props(val formInstances: FormInstancesState) : RProps

private val formInstances = createContext<Pair<FormInstancesState, Unit>>()
internal fun useFormInstancesState() = useContext(formInstances)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.formInstances, Unit))
  child(createElement(formInstances.Provider, configObject, props.children))
}
