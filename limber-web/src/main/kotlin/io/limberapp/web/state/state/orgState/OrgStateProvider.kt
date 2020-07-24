package io.limberapp.web.state.state.orgState

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.orgStateProvider(org: OrgState, children: RHandler<Props>) {
  child(component, Props(org), handler = children)
}

internal data class Props(val org: OrgState) : RProps

private val org = createContext<Pair<OrgState, Unit>>()
internal fun useOrgState() = useContext(org)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.org, Unit))
  child(createElement(org.Provider, configObject, props.children))
}
