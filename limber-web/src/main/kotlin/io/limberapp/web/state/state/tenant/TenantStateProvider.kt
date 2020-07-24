package io.limberapp.web.state.state.tenant

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.tenantStateProvider(tenant: TenantState, children: RHandler<Props>) {
  child(component, Props(tenant), handler = children)
}

internal data class Props(val tenant: TenantState) : RProps

private val tenant = createContext<Pair<TenantState, Unit>>()
internal fun useTenantState() = useContext(tenant)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.tenant, Unit))
  child(createElement(tenant.Provider, configObject, props.children))
}
