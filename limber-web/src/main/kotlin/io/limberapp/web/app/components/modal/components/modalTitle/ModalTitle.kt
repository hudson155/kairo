package io.limberapp.web.app.components.modal.components.modalTitle

import io.limberapp.web.app.components.layout.components.layoutSectionTitle.layoutSectionTitle
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * The title at the top of a modal, plus an optional description. All modals should have one of these.
 */
internal fun RBuilder.modalTitle(title: String, description: String? = null) {
    child(component, Props(title, description))
}

internal data class Props(val title: String, val description: String?) : RProps

private val component = functionalComponent<Props> { props ->
    layoutSectionTitle(props.title, props.description)
}
