package io.limberapp.web.app.components.modal.components.modalTitle

import io.limberapp.web.app.components.layout.components.layoutSectionTitle.layoutSectionTitle
import react.*

internal fun RBuilder.modalTitle(title: String, description: String? = null) {
  child(component, Props(title, description))
}

internal data class Props(val title: String, val description: String?) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  layoutSectionTitle(props.title, props.description)
}
