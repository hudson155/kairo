package io.limberapp.web.state.state.user

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.userStateProvider(user: UserState, children: RHandler<Props>) {
  child(component, Props(user), handler = children)
}

internal data class Props(val user: UserState) : RProps

private val user = createContext<Pair<UserState, Unit>>()
internal fun useUserState() = useContext(user)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.user, Unit))
  child(createElement(user.Provider, configObject, props.children))
}
