package io.limberapp.web.state.state.users

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.usersStateProvider(users: UsersState, children: RHandler<Props>) {
  child(component, Props(users), handler = children)
}

internal data class Props(val users: UsersState) : RProps

private val users = createContext<Pair<UsersState, Unit>>()
internal fun useUsersState() = useContext(users)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.users, Unit))
  child(createElement(users.Provider, configObject, props.children))
}
