package io.limberapp.web.state.state.formInstances

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formInstance.summary
import io.limberapp.web.api.useApi
import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.formInstancesStateProvider(formInstances: FormInstancesState, children: RHandler<Props>) {
  child(component, Props(formInstances), handler = children)
}

internal data class Props(val formInstances: FormInstancesState) : RProps

private val formInstances = createContext<Pair<FormInstancesState, FormInstancesMutator>>()
internal fun useFormInstancesState() = useContext(formInstances)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()

  val (state, setState) = useState(props.formInstances);

  val mutator = object : FormInstancesMutator {
    override suspend fun post(featureGuid: UUID, rep: FormInstanceRep.Creation) =
      api(FormInstanceApi.Post(featureGuid, rep))
        .map {
          setState(state + (it.guid to it.summary()))
          return@map Outcome.Success(it.guid)
        }
  }

  val configObject = ProviderValue(Pair(state, mutator))
  child(createElement(formInstances.Provider, configObject, props.children))
}
