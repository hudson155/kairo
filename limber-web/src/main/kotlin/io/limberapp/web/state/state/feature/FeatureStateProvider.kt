package io.limberapp.web.state.state.feature

import io.limberapp.web.state.ProviderValue
import react.*

internal fun RBuilder.featureStateProvider(feature: FeatureState, children: RHandler<Props>) {
  child(component, Props(feature), handler = children)
}

internal data class Props(val feature: FeatureState) : RProps

private val feature = createContext<Pair<FeatureState, Unit>>()
internal fun useFeatureState() = useContext(feature)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val configObject = ProviderValue(Pair(props.feature, Unit))
  child(createElement(feature.Provider, configObject, props.children))
}
