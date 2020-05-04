package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formTemplatesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.functionalComponent

/**
 * Lists all form templates within the feature.
 */
internal fun RBuilder.formTemplatesListPage() {
    child(formTemplatesListPage)
}

private val formTemplatesListPage = functionalComponent<RProps> {
    standardLayout {
        layoutTitle("Forms")
        p { +"Here's a list of form templates." }
    }
}
