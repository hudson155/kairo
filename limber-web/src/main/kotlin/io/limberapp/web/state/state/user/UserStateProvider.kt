package io.limberapp.web.state.state.user

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.api.useApi
import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.userStateProvider(user: UserState, children: RHandler<Props>) {
  child(component, Props(user), handler = children)
}

internal data class Props(val user: UserState) : RProps

private val user = createContext<Pair<UserState, UserMutator>>()
internal fun useUserState() = useContext(user)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()

  val (state, setState) = useState(props.user)

  val mutator = object : UserMutator {

    override suspend fun patch(userGuid: UUID, rep: UserRep.Update) =
      api(UserApi.Patch(userGuid, rep)).map { user ->
        setState(user)
        return@map Outcome.Success(Unit)
      }
  }

  val configObject = ProviderValue(Pair(state, mutator))
  child(createElement(user.Provider, configObject, props.children))
}
